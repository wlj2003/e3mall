package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理Service
 * <p>Title: ContentCategoryServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<TreeNode> getContentCategoryList(long parentId) {
		// 1、根据parentid查询节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		// 2、转换成TreeNode列表
		List<TreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			TreeNode node = new TreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到返回结果列表
			resultList.add(node);
		}
		// 3、返回列表
		return resultList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		// 1、创建tb_content_category表对应的pojo对象。
		TbContentCategory contentCategory = new TbContentCategory();
		// 2、补全pojo的属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//1(正常),2(删除)
		contentCategory.setStatus(1);
		//排序方式默认值为1
		contentCategory.setSortOrder(1);
		//新添加的节点isparent一定是false
		contentCategory.setIsParent(false);
		contentCategory.setCreated( new Date());
		contentCategory.setUpdated(new Date());
		// 3、向数据库中插入数据。
		contentCategoryMapper.insert(contentCategory);
		// 4、判断父节点的状态，如果不是父节点应该改成父节点。
		//查询父节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			//更新到数据库中
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		// 5、返回结果
		return E3Result.ok(contentCategory);
	}

}
