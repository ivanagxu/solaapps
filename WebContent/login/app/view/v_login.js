Ext.define('login.view.v_login', {
	extend : 'Ext.form.Panel',

	alias : 'widget.loginform',

	title : '系统登入',
	
	id: 'loginForm',
	

	initComponent : function() {

		this.layout = 'table';

		this.items = [ {
			colspan : 3,
			border : false,
			height : '200'
		}, {
			border : false,
			width: '200'
		}, {
			border : false,
			items : [ {
				xtype : 'textfield',
				name : 'login_id',
				fieldLabel : '用户',
				labelWidth:50
			}, {
				xtype : 'textfield',
				name : 'password',
				fieldLabel : '密码',
				inputType: 'password',
				labelWidth:50
			}, {
				xtype : 'button',
				text : '登入',
				id : 'btnLogin'
			} , {
				xtype : 'button',
				text : '取消'
			} ]
		}, {
			border : false
		}, {
			colspan : 3,
			border : false,
			height: 400
		} ];
		


		this.callParent(arguments);
	}
});