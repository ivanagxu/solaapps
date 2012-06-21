Ext.define('document.view.v_document', {
	extend : 'Ext.form.Panel',

	alias : 'widget.documentform',

	title : '',

	id : 'documentform',

	initComponent : function() {
		this.layout = 'anchor';
		
		var sm5 = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
		
		this.items = [ Ext.create('Ext.tab.Panel', {
			anchor : '100%',
			items : [{
				title : '文档管理',
				containScroll: true,
	        	autoScroll: true,
				items : [ Ext.create('Ext.grid.Panel', { 
		        	id : 'document-grid',
		        	height : GET_HEIGHT(), 
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
						width : 800,
						hidden : true
					}],
					tbar : [
						{
					    	xtype : 'button',
					    	text : '添加文档',
					    	hidden : true
						},
						{
							xtype : 'button',
					    	text : '下载文档'
						},
						{
							xtype : 'button',
					    	text : '删除文档',
					    	hidden : true
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
