//productionlog app

Ext.application({
	name : 'productionlog',

	appFolder : 'productionlog/app',

	controllers : [ 'c_productionlog'],
	
	launch : function() {
		Ext.create('Ext.container.Viewport', {
			containScroll : true,
			autoScroll : true,
			items : [ {
				xtype : 'panel',
				items : [ {
					xtype : 'header'
				}, {
					xtype : 'productionlogform'
				} ]
			} ]
		});
	}
});