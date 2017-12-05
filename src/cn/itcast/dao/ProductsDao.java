package cn.itcast.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.itcast.pojo.Page;

public interface ProductsDao {

	public Page queryProductsWithCondition(SolrQuery query);
}
