//login app

Ext.application({
	name : 'login',

	appFolder : 'login/app',

	controllers : [ 'c_login' ],
	launch : function() {
		Ext.create('Ext.container.Viewport', {
			containScroll : true,
			autoScroll : true,
			items : [ {
				xtype : 'panel',
				items : [ {
					xtype : 'header'
				}, {
					xtype : 'loginform'
				} ]
			} ]
		});
	}
});