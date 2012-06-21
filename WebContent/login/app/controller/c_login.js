Ext.define('login.controller.c_login', {
    extend: 'Ext.app.Controller',

    views: [
            'v_login'
        ],
    
    init: function() {
        this.control({
            'viewport > panel': {
                render: this.onPanelRendered
            },
            'button[text="登入"]' :{
            	click: this.onLoginBtnClick
            },
            'button[text="取消"]' :{
            	click: this.onCancelBtnClick
            },
            'textfield[name="login_id"]' :{
            	specialkey: this.onTxtLoginSpecialKey
            },
            'textfield[name="password"]' :{
            	specialkey: this.onTxtLoginSpecialKey
            }
        });
    },

    onPanelRendered: function() {
    	
    },
    
    onLoginBtnClick : function(btn) {
    	btn.up('viewport').down('form[id="loginForm"]').getForm().submit({
    		url : 'UserACController?action=login',
    		success : function(form, resp) {
    			try
    			{
    				SESSION_USER = Ext.JSON.decode(resp.response.responseText).data;
    				if(HAS_PERMISSION("销售部", false))
					{
    					location.href = 'order.jsp';
					}
    				else if(HAS_PERMISSION("生产部", false))
					{
    					location.href = 'production.jsp';
					}
    				else
					{
						location.href = 'job.jsp';
					}
    			}catch(e)
    			{
    				Ext.Msg.alert('登陆', '用户信息错误');
    			}
			},
			failure : function(form, resp) {
				Ext.Msg.alert('登陆',	resp.result.msg);
			}
    	});
    },
    
    onTxtLoginSpecialKey: function(field, e){
        if (e.getKey() == e.ENTER) {
        	this.onLoginBtnClick(field);
        }
    },
    
    onCancelBtnClick : function(btn){
    	btn.up('viewport').down('form[id="loginForm"]').getForm().reset();
    }
});