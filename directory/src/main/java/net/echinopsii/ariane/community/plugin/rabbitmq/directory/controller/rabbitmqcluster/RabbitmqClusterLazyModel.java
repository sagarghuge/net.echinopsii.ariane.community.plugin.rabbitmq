/**
 * RabbitMQ plugin directory bundle
 * Directories RabbitMQ Cluster PrimeFaces Lazy Model
 * Copyright (C) 2014 Mathilde Ffrench
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.echinopsii.ariane.community.plugin.rabbitmq.directory.controller.rabbitmqcluster;

import net.echinopsii.ariane.community.plugin.rabbitmq.directory.RabbitmqDirectoryBootstrap;
import net.echinopsii.ariane.community.plugin.rabbitmq.directory.model.RabbitmqCluster;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RabbitmqClusterLazyModel extends LazyDataModel<RabbitmqCluster> {
    private static final Logger log = LoggerFactory.getLogger(RabbitmqClusterLazyModel.class);

    private int              rowCount      ;
    private List<RabbitmqCluster> pageItems ;

    private Predicate[] getSearchPredicates(EntityManager entityManager, Root<RabbitmqCluster> root, Map<String,String> filters) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = filters.get(filterProperty);
            log.debug("Filter : { {}, {} }", new Object[]{filterProperty, filterValue});
            predicatesList.add(builder.like(root.<String> get(filterProperty), '%' + filterValue + '%'));
        }
        Predicate[] ret = predicatesList.toArray(new Predicate[predicatesList.size()]);
        log.debug("Return predicates list: {}", new Object[]{ret.toString()});
        return ret;
    }

    private void paginate(int first, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        EntityManager entityManager = RabbitmqDirectoryBootstrap.getDirectoryJPAProvider().createEM();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Populate this.count
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<RabbitmqCluster> root = countCriteria.from(RabbitmqCluster.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(entityManager,root,filters));
        this.rowCount = (int) (long) entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems
        CriteriaQuery<RabbitmqCluster> criteria = builder.createQuery(RabbitmqCluster.class);
        root = criteria.from(RabbitmqCluster.class);
        criteria.select(root).where(getSearchPredicates(entityManager,root,filters));
        if (sortOrder!=null && sortField!=null)
            criteria.orderBy(sortOrder.toString().equals("DESCENDING") ? builder.desc(root.get(sortField)) : builder.asc(root.get(sortField)));
        TypedQuery<RabbitmqCluster> query = entityManager.createQuery(criteria);
        query.setFirstResult(first).setMaxResults(getPageSize());
        log.debug("Query: {}", new Object[]{query.toString()});
        this.pageItems = query.getResultList();
        entityManager.close();
    }

    @Override
    public RabbitmqCluster getRowData(String rowKey) {
        for(RabbitmqCluster rabbitMQCluster : pageItems) {
            if(rabbitMQCluster.getId().equals(rowKey))
                return rabbitMQCluster;
        }
        return null;
    }

    @Override
    public Object getRowKey(RabbitmqCluster rabbitmqCluster) {
        return rabbitmqCluster.getId();
    }

    @Override
    public List<RabbitmqCluster> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        this.setPageSize(pageSize);
        paginate(first,sortField,sortOrder,filters);
        this.setRowCount(rowCount);
        return pageItems;
    }
}