package controllers.catalog;

import java.util.ArrayList;
import java.util.List;

import models.merchant.Merchant;
import models.user.User;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import com.mnt.core.menu.MenuBarFixture;
import com.mnt.core.ui.component.TreeNode;

import controllers.Secured;
import views.html.catalog.catalogIndex;

@Security.Authenticated(Secured.class)
public class Catalogue extends Controller{

	 public static Result index() {
		 return ok(catalogIndex.render(MenuBarFixture.build(request().username()),
				 Merchant.findByUserName(request().username()),
						 buildDummyTreeNodeAsPOC()));
	 }
	 
	 public static Result doCatalogItemOperation() {
		 DynamicForm form = new DynamicForm().bindFromRequest();
		 String operation = form.get("oprt");
		 if("Add".equals(operation)){
			 return add();
		 }
		 else if("Remove".equals(operation)){
			 return delete();
		 }
		 else if("Edit".equals(operation)){
			 return show();
		 }
		 
		 return ok("not a valid operation");
	 }
	 private static Result show() {
		 DynamicForm form = new DynamicForm().bindFromRequest();
		 return ok("Showing: ref:"+form.get("ref")+ "type: " + form.get("type"));
	 }
	 
	 private static Result delete() {
		 DynamicForm form = new DynamicForm().bindFromRequest();
		 return ok("Delete: ref:"+form.get("ref") + "type: " + form.get("type"));
	 }
	 
	 private static Result add() {
		 DynamicForm form = new DynamicForm().bindFromRequest();
		 return ok("Add Node to: ref:"+form.get("ref") + "type: " + form.get("type"));
	 }
	 
	 private static TreeNode buildDummyTreeNodeAsPOC(){
		 
		 return CatalogTree.me().withName("Catalog").withPositionID(0).withType(0).
				 addNode(CatalogTree.me().withName("Camera").withPositionID(0).withType(1)).
				 addNode(CatalogTree.me().withName("Laptop").withPositionID(0).withType(1).
						 addNode(CatalogTree.me().withName("Sony").withPositionID(0).withType(1).
								 addNode(CatalogTree.me().withName("Sony Vaio 15").withPositionID(0).withType(2)).
								 addNode(CatalogTree.me().withName("Sony Vaio 14 ").withPositionID(0).withType(2))
								 ).
						 addNode(CatalogTree.me().withName("Dell").withPositionID(0).withType(1))
						 ).
				 addNode(CatalogTree.me().withName("Nokia-E5").withPositionID(1002).withType(2));
	 }
	 
	 static class CatalogTree implements TreeNode{

		private CatalogTree(){}
		private String name;
		private List<TreeNode> node;
		private int positionID;
		private int type;
		private long refId;
		
		
		public static CatalogTree  me(){
			return new CatalogTree();
		}
		
		public CatalogTree withName(String name){
			this.name = name;
			return this;
		}
		
		public CatalogTree withPositionID(int positionID){
			this.positionID = positionID;
			return this;
		}
		
		public CatalogTree withRefID(long refID){
			this.refId = refID;
			return this;
		}
		
		public CatalogTree withType(int type){
			this.type = type;
			return this;
		}
		
		public CatalogTree addNode(TreeNode node){
			if(this.node==null){
				this.node = new ArrayList<TreeNode>();
			}
			this.node.add(node);
			return this;
		}
		
		@Override
		public String name() {
			return name;
		}

		@Override
		public boolean isExpandable() {
			return false;
		}

		@Override
		public List<TreeNode> node() {
			return node;
		}

		@Override
		public int positionID() {
			return positionID;
		}

		@Override
		public int _type() {
			return type;
		}

		@Override
		public long refID() {
			return refId;
		}
		 
	 }

}
