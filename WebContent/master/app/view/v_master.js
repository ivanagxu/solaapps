Ext.define('master.view.v_master', {
	extend : 'Ext.form.Panel',

	alias : 'widget.header',

	initComponent : function() {
		this.layout = 'table';

		this.items = [ {
			colspan : 3,
			border : false
		}, {
			border : false
		}, {
			border : false,
			items : [ {
				xtype : 'box',
				html: '<table><tr><td><img src="resources/images/banner.jpg" width=160 height=50/></td>' + 
				'<td align="center" width="120"><a href="#" onclick="GOTO_ORDER_MODULE();" id="link_order">订单管理</a></td>' + 
				'<td align="center" width="120"><a href="#" onclick="GOTO_PRODUCTION_MODULE();" id="link_job">生产管理</a></td>' +
				'<td align="center" width="120"><a href="#" onclick="GOTO_JOB_MODULE();" id="link_job">部门管理</a></td>' +
				'<td align="center" width="120"><a href="#" onclick="GOTO_INVENTORY_MODULE();" id="link_job">仓库管理</a></td>' +
				'<td align="center" width="120"><a href="#" onclick="GOTO_PRODUCTIONLOG_MODULE();" id="link_job">生产记录</a></td>' +
				//'<td align="center" width="120"><a href="#" onclick="GOTO_DOCUMENT_MODULE();" id="link_job">文档管理</a></td>' +
				'<td align="center" width="120"><a href="#" onclick="GOTO_ADMIN_MODULE();" id="link_admin">后台管理</a></td>' +
				'<td align="center" width="120"><a href="#" onclick="LOGOUT();" id="link_logout">系统登出</a></td>' +
				'<td align="right" width="60"></td>' +
				'<td align="left" width="100"><span id="link_user"/></td>' +
				'</tr></table>'
			} ]
		}, {
			border : false
		}, {
			colspan : 3,
			border : false
		} ];

		
		Ext.create('Ext.form.Panel', {
			id : 'init-user-form',
			url : 'UserACController?action=getSessionUser',
			items : []
		});
		
		
		this.callParent(arguments);
		
		Ext.getCmp('init-user-form').getForm().submit({
    		success : function(form, resp) {
    			SESSION_USER = resp.result.data;
    			if(!SESSION_USER && window.location.href.indexOf("login.jsp") <= 0)
				{
    				window.location.href = "login.jsp";
				}
    			try
    			{
    				document.getElementById("link_user").innerHTML = "职位:" + SESSION_USER.post.name + 
    				"<br />名称:" + SESSION_USER.name;
    			}catch(ex)
    			{
    				
    			}
    			
    				
    				
			},
			failure : function(form, resp) {
				SESSION_USER = undefined;
				if(!SESSION_USER && window.location.href.indexOf("login.jsp") <= 0)
				{
    				window.location.href = "login.jsp";
				}
			}
    	});
	}
});



