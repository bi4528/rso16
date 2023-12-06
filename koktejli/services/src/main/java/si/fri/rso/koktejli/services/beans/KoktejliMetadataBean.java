package si.fri.rso.koktejli.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.eclipse.microprofile.metrics.annotation.Timed;

import si.fri.rso.koktejli.lib.KoktejliMetadata;
import si.fri.rso.koktejli.models.converters.KoktejliMetadataConverter;
import si.fri.rso.koktejli.models.entities.KoktejliMetadataEntity;


@RequestScoped
public class KoktejliMetadataBean {

    private Logger log = Logger.getLogger(KoktejliMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    public List<KoktejliMetadata> getKoktejliMetadata() {

        TypedQuery<KoktejliMetadataEntity> query = em.createNamedQuery(
                "KoktejliMetadataEntity.getAll", KoktejliMetadataEntity.class);

        List<KoktejliMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(KoktejliMetadataConverter::toDto).collect(Collectors.toList());

    }

    public List<KoktejliMetadata> getKoktejliMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, KoktejliMetadataEntity.class, queryParameters).stream()
                .map(KoktejliMetadataConverter::toDto).collect(Collectors.toList());
    }

    public KoktejliMetadata getKoktejliMetadata(Integer id) {

        KoktejliMetadataEntity KoktejliMetadataEntity = em.find(KoktejliMetadataEntity.class, id);

        if (KoktejliMetadataEntity == null) {
            throw new NotFoundException();
        }

        KoktejliMetadata KoktejliMetadata = KoktejliMetadataConverter.toDto(KoktejliMetadataEntity);

        return KoktejliMetadata;
    }

    public KoktejliMetadata createKoktejliMetadata(KoktejliMetadata KoktejliMetadata) {

        KoktejliMetadataEntity KoktejliMetadataEntity = KoktejliMetadataConverter.toEntity(KoktejliMetadata);

        try {
            beginTx();
            em.persist(KoktejliMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (KoktejliMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return KoktejliMetadataConverter.toDto(KoktejliMetadataEntity);
    }

    public KoktejliMetadata putKoktejliMetadata(Integer id, KoktejliMetadata KoktejliMetadata) {

        KoktejliMetadataEntity c = em.find(KoktejliMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        KoktejliMetadataEntity updatedKoktejliMetadataEntity = KoktejliMetadataConverter.toEntity(KoktejliMetadata);

        try {
            beginTx();
            updatedKoktejliMetadataEntity.setId(c.getId());
            updatedKoktejliMetadataEntity = em.merge(updatedKoktejliMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return KoktejliMetadataConverter.toDto(updatedKoktejliMetadataEntity);
    }

    public boolean deleteKoktejliMetadata(Integer id) {

        KoktejliMetadataEntity KoktejliMetadata = em.find(KoktejliMetadataEntity.class, id);

        if (KoktejliMetadata != null) {
            try {
                beginTx();
                em.remove(KoktejliMetadata);
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
