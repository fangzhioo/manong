package com.manong.service.impl;

import com.manong.mapper.ProductCategoryMapper;
import com.manong.pojo.ProductCategory;
import com.manong.pojo.ProductCategoryExample;
import com.manong.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.EasyUITree;
import pojo.ResponseJsonResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<EasyUITree> findProductCategoryListByParentId(Short parentid) {

        ProductCategoryExample productCategoryExample = new ProductCategoryExample();
        ProductCategoryExample.Criteria criteria = productCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentid);

        List<ProductCategory> productCategoryList = productCategoryMapper.selectByExample(productCategoryExample);

        List<EasyUITree> easyUITrees = new ArrayList<>(productCategoryList.size());
        for(ProductCategory productCategory:productCategoryList){
            EasyUITree easyUITree = new EasyUITree();
            easyUITree.setId(productCategory.getId());
            easyUITree.setText(productCategory.getName());
            easyUITree.setState(productCategory.getParentId()==0?"closed":"open");
            easyUITree.setAttributes(productCategory.getParentId().toString());
            easyUITrees.add(easyUITree);
        }
        return easyUITrees;
    }

    @Override
    public ResponseJsonResult addCategory(Short parentid, String name) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setParentId(parentid);
        productCategory.setName(name);

        productCategoryMapper.insert(productCategory);

        ResponseJsonResult responseJsonResult = new ResponseJsonResult();
        responseJsonResult.setMsg(productCategory.getId().toString());

        return responseJsonResult;
    }

    @Override
    public ResponseJsonResult deleteCategory(Short parentid, Short id) {

        ProductCategoryExample productCategoryExample = new ProductCategoryExample();
        ProductCategoryExample.Criteria criteria = productCategoryExample.createCriteria();

        if(parentid == 0){
            // 父类
            criteria.andIdEqualTo(id);
            ProductCategoryExample.Criteria criteria1 = productCategoryExample.createCriteria();
            criteria1.andParentIdEqualTo(id);
            productCategoryExample.or(criteria1);
        }else{
            criteria.andIdEqualTo(id);
        }

        productCategoryMapper.deleteByExample(productCategoryExample);

        ResponseJsonResult responseJsonResult = new ResponseJsonResult();
        responseJsonResult.setStatus(200);
        responseJsonResult.setMsg("success");

        return responseJsonResult;
    }

    @Override
    public ResponseJsonResult updateCategory(Short id, String name) {

        ProductCategory productCategory = productCategoryMapper.selectByPrimaryKey(id);
        if(productCategory == null){
            return null;
        }else{
            ProductCategoryExample productCategoryExample = new ProductCategoryExample();
            ProductCategoryExample.Criteria criteria = productCategoryExample.createCriteria();
            criteria.andIdEqualTo(id);

            productCategory.setName(name);
            productCategoryMapper.updateByExample(productCategory,productCategoryExample);

            ResponseJsonResult responseJsonResult = new ResponseJsonResult();
            responseJsonResult.setStatus(200);
            responseJsonResult.setMsg(productCategory.getId().toString());

            return responseJsonResult;
        }
    }

}
