package si.fri.rso.seznam.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import si.fri.rso.seznam.lib.CocktailDBResponse;
import si.fri.rso.seznam.lib.CocktailDB;
import si.fri.rso.seznam.lib.SeznamMetadata;
import si.fri.rso.seznam.models.converters.SeznamMetadataConverter;
import si.fri.rso.seznam.models.entities.SeznamMetadataEntity;
import si.fri.rso.seznam.services.config.AppConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

@Log
@RequestScoped
public class SeznamMetadataBean {

    private Logger log = Logger.getLogger(SeznamMetadataBean.class.getName());

    @Inject
    private AppConfig appConfig;

    @Inject
    private EntityManager em;

    public List<SeznamMetadata> getSeznamMetadata() {

        TypedQuery<SeznamMetadataEntity> query = em.createNamedQuery(
                "SeznamMetadataEntity.getAll", SeznamMetadataEntity.class);

        List<SeznamMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(SeznamMetadataConverter::toDto).collect(Collectors.toList());

    }

    public List<SeznamMetadata> getSeznamMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, SeznamMetadataEntity.class, queryParameters).stream()
                .map(SeznamMetadataConverter::toDto).collect(Collectors.toList());
    }

    public SeznamMetadata getSeznamMetadata(Integer id) {

        SeznamMetadataEntity SeznamMetadataEntity = em.find(SeznamMetadataEntity.class, id);

        if (SeznamMetadataEntity == null) {
            throw new NotFoundException();
        }

        SeznamMetadata SeznamMetadata = SeznamMetadataConverter.toDto(SeznamMetadataEntity);

        return SeznamMetadata;
    }
    public SeznamMetadata getSeznamMetadataByUser(String user) {

        SeznamMetadataEntity SeznamMetadataEntity = em.find(SeznamMetadataEntity.class, user);

        if (SeznamMetadataEntity == null) {
            throw new NotFoundException();
        }

        SeznamMetadata SeznamMetadata = SeznamMetadataConverter.toDto(SeznamMetadataEntity);

        return SeznamMetadata;
    }

    public CocktailDBResponse getCocktailDBResponseById(String id) {
        HttpClient client = HttpClient.newHttpClient();
        String url = appConfig.getKoktejliServiceUrl() + "/id/" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            CocktailDBResponse cocktailDBResponse = parseCocktailDBResponse(response.body());

            if (cocktailDBResponse == null) {
                throw new NotFoundException();
            }

            return cocktailDBResponse;

        } catch (IOException | InterruptedException e) {
            log.severe("Error while getting cocktailDBResponse: " + e.getMessage());
            return null;
        }
    }

    private CocktailDBResponse parseCocktailDBResponse(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CocktailDBResponse response = objectMapper.readValue(jsonResponse, CocktailDBResponse.class);
            if (response != null && response.getDrinks() != null && !response.getDrinks().isEmpty()) {
                return response;
            }
        } catch (Exception e) {
            log.severe("Error while parsing cocktailDBResponse: " + e.getMessage());
        }
        return null;
    }

    public SeznamMetadata createSeznamMetadata(Integer id, String user) {

        CocktailDBResponse cocktailDBResponse = getCocktailDBResponseById(Integer.toString(id));
        CocktailDB cocktailDB = cocktailDBResponse.getDrinks().get(0);
        SeznamMetadata seznamMetadata = new SeznamMetadata();
        seznamMetadata.setCocktailId(cocktailDB.getIdDrink());
        seznamMetadata.setName(cocktailDB.getStrDrink());
        seznamMetadata.setUser(user);

        SeznamMetadataEntity SeznamMetadataEntity = SeznamMetadataConverter.toEntity(seznamMetadata);

        try {
            beginTx();
            em.persist(SeznamMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (SeznamMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return SeznamMetadataConverter.toDto(SeznamMetadataEntity);
    }

    public SeznamMetadata putSeznamMetadata(Integer id, SeznamMetadata SeznamMetadata) {

        SeznamMetadataEntity c = em.find(SeznamMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        SeznamMetadataEntity updatedSeznamMetadataEntity = SeznamMetadataConverter.toEntity(SeznamMetadata);

        try {
            beginTx();
            updatedSeznamMetadataEntity.setId(c.getId());
            updatedSeznamMetadataEntity = em.merge(updatedSeznamMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return SeznamMetadataConverter.toDto(updatedSeznamMetadataEntity);
    }

    public boolean deleteSeznamMetadata(Integer id) {

        SeznamMetadataEntity SeznamMetadata = em.find(SeznamMetadataEntity.class, id);

        if (SeznamMetadata != null) {
            try {
                beginTx();
                em.remove(SeznamMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
