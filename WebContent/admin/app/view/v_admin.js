Ext.define('admin.view.v_admin', {
	extend : 'Ext.form.Panel',

	alias : 'widget.adminform',

	title : '',

	id : 'adminform',

	initComponent : function() {
		this.layout = 'anchor';
		
		var sm1 = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
		var sm2 = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
		var sm3 = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
		var sm4 = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
		var sm5 = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
		
		this.items = [ Ext.create('Ext.tab.Panel', {
			anchor : '100%',
			items : [ {
				title : '产品管理',
				items : [ Ext.create('Ext.grid.Panel', {
					id : 'product-grid',
					height : GET_HEIGHT(), 
					store : Ext.data.StoreManager.lookup('allProductStore'),
					selModel : sm1,
					columns : [ {
						header : '客户料号',
						dataIndex : 'name'
					}, {
						header : '英文名',
						dataIndex : 'name_eng',
						hidden: true,
					}, {
						header : '料号',
						dataIndex : 'our_name'
					}, {
						header : '模具率',
						dataIndex : 'mold_rate'
					}, {
						header : '机加位',
						dataIndex : 'machining_pos'
					}, {
						header : '手工位',
						dataIndex : 'handwork_pos'
					}, {
						header : '抛光难度',
						dataIndex : 'polishing'
					}, {
						header : '产品图片',
						dataIndex : 'image',
						renderer: function(val)
						{
							if(val == '')
								return '';
							else
								return '<a target="_blank" onclick=DISPLAY_IMAGE_WINDOW("ProductController?action=getProductImage&name=' + val + '",' + '"ProductController?action=getProductDrawing&name=' + val + '"' + ')><img src="resources/images/picture.png"/></a>';
						}
					}, {
						header : '产品图纸',
						dataIndex : 'drawing',
						hidden : true,
						renderer: function(val)
						{
							if(val == '')
								return '';
							else
								return '<a target="_blank" onclick=DISPLAY_IMAGE_WINDOW("ProductController?action=getProductImage&name=' + val + '",' + '"ProductController?action=getProductDrawing&name=' + val + '"' + ')><img src="resources/images/picture.png"/></a>';
						}
					}, {
						header : '状态',
						dataIndex : 'status'
					} ,{
						header : '已有半成品',
						dataIndex : 'semi_finished'
					} ,{
						header : '已有成品',
						dataIndex : 'finished'
					} ,{
						header : '模具号码',
						dataIndex : 'mold_code'
					} ,{
						header : '模具名称',
						dataIndex : 'mold_name'
					} ,{
						header : '模具架号',
						dataIndex : 'mold_stand_no'
					} ],
					tbar : [ {
						text : '添加产品',
						xtype: 'button'
					}, {
						text : '修改产品',
						xtype: 'button'
					}, {
						text : '删除产品',
						xtype: 'button'
					} ]
				}) ]
			}, {
				title : '客户管理',
				items : [ 
		        Ext.create('Ext.grid.Panel', { 
		        	id : 'customer-grid',
		        	height : GET_HEIGHT(), 
		        	store: Ext.data.StoreManager.lookup('allCustomerStore'),
		        	selModel : sm2,
		        	columns : [ {
						header : 'ID',
						dataIndex : 'id',
						hidden : true
					} ,{
						header : '客户名称',
						dataIndex : 'name'
					}, {
						header : '客户代码',
						dataIndex : 'code'
					}, {
						header : '联络电话',
						dataIndex : 'phone'
					}, {
						header : '传真号码',
						dataIndex : 'fax'
					}, {
						header : '电子邮件',
						dataIndex : 'email'
					}, {
						header : '联络人',
						dataIndex : 'contact_person'
					}],
					tbar : [
					    {
					    	text : '添加客户',
					    	xtype : 'button'
					    },
					    {
					    	text : '修改客户',
					    	xtype : 'button',
					    	hidden : true,
					    },
					    {
					    	text : '删除客户',
					    	xtype : 'button'
					    }
		            ]
		        })
		        ]
			}, {
				title : '用户管理',
				containScroll: true,
	        	autoScroll: true,
				items : [ 
			        Ext.create('Ext.grid.Panel', { 
			        	id : 'user-grid',
			        	height : GET_HEIGHT(), 
			        	store: Ext.data.StoreManager.lookup('allUserACStore'),
			        	selModel : sm3,
			        	columns : [ {
							header : 'ID',
							dataIndex : 'id',
							hidden : true
						} ,{
							header : '用户ID',
							dataIndex : 'login_id'
						}, {
							header : '用户名称',
							dataIndex : 'name'
						}, {
							header : '职位',
							dataIndex : 'post'
						}, {
							header : '公司',
							dataIndex : 'department'
						}, {
							header : '分部',
							dataIndex : 'division'
						}, {
							header : '部门',
							dataIndex : 'section'
						}, {
							header : '角色',
							dataIndex : 'role'
						}, {
							header : '电子邮件',
							dataIndex : 'email'
						}, {
							header : '性别',
							dataIndex : 'sex'
						}, {
							header : '出生日期',
							dataIndex : 'birthday'
						}, {
							header : '工资',
							dataIndex : 'salary'
						}, {
							header : '雇用日期',
							dataIndex : 'employ_date'
						}, {
							header : '状态',
							dataIndex : 'status'
						}, {
							header : '创建日期',
							dataIndex : 'update_date'
						}, {
							header : '修改日期',
							dataIndex : 'create_date'
						}],
						tbar : [
						    {
						    	text : '添加用户',
						    	xtype : 'button'
						    },
						    {
						    	text : '修改用户',
						    	xtype : 'button',
						    	hidden : true
						    },
						    {
						    	text : '删除用户',
						    	xtype : 'button'
						    }
			            ]
			        })
			        ]
			},{
				title: '模具管理',
				height : GET_HEIGHT(), 
				items : [ Ext.create('Ext.grid.Panel', { 
		        	id : 'mold-grid',
		        	store: Ext.data.StoreManager.lookup('allMoldStore'),
		        	selModel : sm4,
		        	columns : [ {
						header : '模具号码',
						dataIndex : 'code'
					} ,{
						header : '模具名称',
						dataIndex : 'name'
					}, {
						header : '所在架号',
						dataIndex : 'stand_no'
					}],
					tbar : [
					    {
					    	text : '添加模具',
					    	xtype : 'button'
					    },
					    {
					    	text : '删除模具',
					    	xtype : 'button'
					    }
		            ]
		        })]
			}, {
				title : '文档管理',
				height : GET_HEIGHT(), 
	        	hidden : true,
				items : [ Ext.create('Ext.grid.Panel', { 
		        	id : 'document-grid',
		        	store: Ext.data.StoreManager.lookup('documentStore'),
		        	selModel : sm5,
		        	columns : [ {
						header : '文件类型',
						dataIndex : 'type'
					} ,{
						header : '文件名称',
						dataIndex : 'name',
						width : 200
					}, {
						header : '文件路径',
						dataIndex : 'full_path',
						width : 800
					}],
					tbar : [
						{
					    	xtype : 'button',
					    	text : '添加文档'
						},
						{
							xtype : 'button',
					    	text : '下载文档'
						},
						{
							xtype : 'button',
					    	text : '删除文档'
						},
						{
							xtype : 'button',
					    	text : '刷新文档'
						}
		            ]
		        })]
			} ]
		}) ];

		this.callParent(arguments);
	}
});
