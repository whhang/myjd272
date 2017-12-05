package cn.itcast.service;


import cn.itcast.pojo.Page;

public interface ProductsService {

	public Page queryPageWithCondition(String queryString,
								String catalog_name,
								String price,
								Integer page,
								Integer rows,
								String sort);
}
