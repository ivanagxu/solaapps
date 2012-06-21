Ext.define('inventory.view.v_inventory', {
	extend : 'Ext.form.Panel',

	alias : 'widget.inventory_table',

	title : '',

	id : 'inventory_table',

	initComponent : function() {
		this.layout = 'anchor';
		var sm1 = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
		
		this.items = [ Ext.create('Ext.tab.Panel', {
			anchor : '100%',
			items : [ {
				title : '产品管理',
				items : [ Ext.create('Ext.grid.Panel', {
					id : 'inventory-grid',
					height : GET_HEIGHT(), 
					store : Ext.data.StoreManager.lookup('allProductStore'),
					selModel : sm1,
					columns : [ {
						header : '料号',
						dataIndex : 'our_name'
					}, {
						header : '模具率',
						dataIndex : 'mold_rate',
						hidden : true
					}, {
						header : '机加位',
						dataIndex : 'machining_pos',
						hidden : true
					}, {
						header : '手工位',
						dataIndex : 'handwork_pos',
						hidden : true
					}, {
						header : '抛光难度',
						dataIndex : 'polishing',
						hidden : true
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
						header : '已有半成品',
						dataIndex : 'semi_finished'
					} ,{
						header : '已有成品',
						dataIndex : 'finished'
					} ,{
						header : '模具号码',
						dataIndex : 'mold_code',
						hidden : true
					} ,{
						header : '模具名称',
						dataIndex : 'mold_name',
						hidden : true
					} ,{
						header : '模具架号',
						dataIndex : 'mold_stand_no',
						hidden : true
					}, {
						header : '状态',
						dataIndex : 'status'
					}],
					tbar : [ {
						text : '出仓',
						xtype: 'button'
					}]
		        })]
			} ]
		}) ];
		this.callParent(arguments);
	}
});
