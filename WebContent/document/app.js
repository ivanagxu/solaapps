//document app

Ext.application({
	name : 'document',

	appFolder : 'document/app',

	controllers : [ 'c_document'],
	
	launch : function() {
		Ext.create('Ext.container.Viewport', {
			containScroll : true,
			autoScroll : true,
			items : [ {
				xtype : 'panel',
				items : [ {
					xtype : 'header'
				}, {
					xtype : 'documentform'
				} ]
			} ]
		});
	}
});