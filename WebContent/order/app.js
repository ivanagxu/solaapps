//order app

Ext.application({
	name : 'order',

	appFolder : 'order/app',

	controllers : [ 'c_order' ],
	launch : function() {
		Ext.create('Ext.container.Viewport', {
			containScroll : true,
			autoScroll : true,
			items : [ {
				xtype : 'panel',
				items : [ {
					xtype : 'header'
				}, {
					xtype : 'order_table'
				} ]
			} ]
		});
	}
});