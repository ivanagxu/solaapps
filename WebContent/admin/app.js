//admin app

Ext.application({
	name : 'admin',

	appFolder : 'admin/app',

	controllers : [ 'c_admin' ,'c_product', 'c_customer', 'c_user', 'c_mold', 'c_document'],
	launch : function() {
		Ext.create('Ext.container.Viewport', {
			containScroll : true,
			autoScroll : true,
			items : [ {
				xtype : 'panel',
				items : [ {
					xtype : 'header'
				}, {
					xtype : 'adminform'
				} ]
			} ]
		});
	}
});