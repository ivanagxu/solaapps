Ext.define('admin.controller.c_admin', {
	extend : 'Ext.app.Controller',

	views : [ 'v_admin' ],

	init : function() {
		this.control({
			'viewport > panel' : {
				render : this.onPanelRendered
			}
		});
	},

	onPanelRendered : function() {
		//console.log('The admin module was rendered');
	}
	
});