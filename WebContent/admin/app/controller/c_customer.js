Ext.define('admin.controller.c_customer', {
	extend : 'Ext.app.Controller',

	views : [ 'v_admin' ],

	init : function() {
		this.control({
			'panel[id=customer-grid]' : {
				render : this.onCustomerGridRendered
			},
			'button[text=添加客户]' : {
				click : this.onAddCustomerClick
			},
			'button[text=修改客户]' : {
				click : this.onEditCustomerClick
			},
			'button[text=删除客户]' : {
				click : this.onDeleteCustomerClick
			}
		});
	},
	
	onCustomerGridRendered : function() {
		Ext.data.StoreManager.lookup('allCustomerStore').load();
	},
	
	onAddCustomerClick : function() {
		var win = Ext.create('Ext.window.Window', {
			title : '客户资料',
			height : 240,
			width : 300,
			layout : 'fit',
			modal: true,
			items : [ Ext.create('Ext.form.Panel', {
				layout : 'anchor',
				baseCls: 'x-plain',
				border : false,
				containScroll : true,
				autoScroll : true,
				defaults : {
					anchor : '98%'
				},
				items : [ {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '客户名称',
					name : 'name'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '客户代码',
					name : 'code'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '联络电话',
					name : 'phone'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '传真号码',
					name : 'fax'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '电子邮件',
					name : 'email'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '联系人',
					name : 'contact_person'
				}],
				buttons : [ {
					text : '确定',
					handler : function() {
						var form = this.up('form').getForm();
						
						if(Ext.String.trim(this.up('form').down('textfield[name="name"]').getValue()) == '' || 
							Ext.String.trim(this.up('form').down('textfield[name="code"]').getValue()) == ''
							)
						{
							Ext.Msg.alert('添加结果','请输入客户代码或客户名称');
							return;
						}
						
						form.submit({
							url : 'CustomerController?action=addCustomer',
							success : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allCustomerStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allCustomerStore').load();
							}
						});
					}
				}, {
					text : '取消',
					handler : function() {
						win.close();
					}
				} ]
			}) ]
		});
		win.show();
	},
	
	onEditCustomerClick : function(){
		var win = Ext.create('Ext.window.Window', {
			title : '客户资料',
			height : 240,
			width : 300,
			layout : 'fit',
			modal: true,
			items : [ Ext.create('Ext.form.Panel', {
				layout : 'anchor',
				baseCls: 'x-plain',
				border : false,
				containScroll : true,
				autoScroll : true,
				defaults : {
					anchor : '98%'
				},
				items : [
		        {
		        	xtype : 'hidden',
		        	name : 'id'
		        }, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '客户名称',
					name : 'name'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '客户代码',
					name : 'code',
					readOnly : true
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '联络电话',
					name : 'phone'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '传真号码',
					name : 'fax'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '电子邮件',
					name : 'email'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '联系人',
					name : 'contact_person'
				}],
				buttons : [ {
					text : '确定',
					handler : function() {
						var form = this.up('form').getForm();
						
						if(Ext.String.trim(this.up('form').down('textfield[name="name"]').getValue()) == '' || 
							Ext.String.trim(this.up('form').down('textfield[name="code"]').getValue()) == ''
							)
						{
							Ext.Msg.alert('修改结果','请输入客户名称和客户代码');
							return;
						}
						
						form.submit({
							url : 'CustomerController?action=updateCustomer',
							success : function(form, resp) {
								Ext.Msg.alert('修改结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allCustomerStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('修改结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allCustomerStore').load();
							}
						});
					}
				}, {
					text : '取消',
					handler : function() {
						win.close();
					}
				} ]
			}) ]
		});
		
		var selected = Ext.getCmp('customer-grid').getSelectionModel().getSelection();
		if(selected.length == 0)
		{
			Ext.Msg.alert("修改客户","请选择要修改的客户");
			return;
		}
		else
		{
			function fillForm(data)
			{
				if(!data)
				{
					Ext.Msg.alert("修改客户", "获取客户详细资料失败");
				}
				else
				{
					win.down('hiddenfield[name="id"]').setValue(data.id);
					win.down('textfield[name="name"]').setValue(data.name);
					win.down('textfield[name="code"]').setValue(data.code);
					win.down('textfield[name="phone"]').setValue(data.phone);
					win.down('textfield[name="fax"]').setValue(data.fax);
					win.down('textfield[name="email"]').setValue(data.email);
					win.down('textfield[name="contact_person"]').setValue(data.contact_person);
				}
				win.show();
			}	
			GetJsonData("CustomerController?action=getCustomerById",{id: selected[0].data.id}, fillForm);
			
		}
	},
	
	onDeleteCustomerClick : function(){
		var win = Ext.create('Ext.window.Window', {
			title : '删除客户',
			height : 60,
			width : 80,
			layout : 'fit',
			modal: true,
			items : [ Ext.create('Ext.form.Panel', {
				layout : 'anchor',
				border : false,
				baseCls: 'x-plain',
				defaults : {
					anchor : '98%'
				},
				items : [
			         {
			        	 xtype : 'box',
			        	 html: '<table><tr height=10><td></td></tr><tr><td>是否删除该客户?</td></tr></table>'
			         },
			         {
			        	 xtype : 'hiddenfield',
			        	 name: 'id'
			         }
		         ],
		         buttons: [
                 {
            	 	 text : '确定',
            	 	 handler : function(){
            	 		 
            	 		 var selected = Ext.getCmp('customer-grid').getSelectionModel().getSelection();
            	 		 if(selected.length == 0)
        	 			 {
            	 			 Ext.Msg.alert("删除客户","请选择要删除的客户");
            	 			 win.close();
            	 			 return;
        	 			 }
            	 		 
            	 		 this.up('form').down('hiddenfield[name="id"]').setValue(selected[0].data.id);
            	 		 
            	 		 this.up('form').getForm().submit({
            	 			url : 'CustomerController?action=deleteCustomer',
							success : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allCustomerStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allCustomerStore').load();
								win.close();
							}
            	 		 });
            	 		 
            	 	 }
                 },
                 {
                	 text : '取消',
                	 handler : function(){
                		 win.close();
                	 }
                 }
		         ]
			})]
		});
		
		var selected = Ext.getCmp('customer-grid').getSelectionModel().getSelection();
		if(selected.length == 0)
		{
			Ext.Msg.alert("删除客户","请选择要删除的客户");
			return;
		}
		else
		{
			win.show();	
		}
	}
});