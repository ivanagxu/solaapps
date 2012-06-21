Ext.define('admin.controller.c_user', {
	extend : 'Ext.app.Controller',

	views : [ 'v_admin' ],


	init : function() {
		this.control({
			'panel[id=user-grid]' : {
				render : this.onUserGridRendered
			},
			'button[text=添加用户]' : {
				click : this.onAddUserButtonClick
			},
			'button[text=修改用户]' : {
				click : this.onEditUserButtonClick
			},
			'button[text=删除用户]' : {
				click : this.onDeleteUserButtonClick
			}
		});
	},

	onUserGridRendered : function() {
		Ext.data.StoreManager.lookup('allUserACStore').load();
	},
	onAddUserButtonClick : function() {
		Ext.data.StoreManager.lookup('allSectionStore').load();
		Ext.data.StoreManager.lookup('allRoleStore').load();
		var win = Ext.create('Ext.window.Window', {
			title : '用户资料',
			height : 600,
			width : 480,
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
					fieldLabel : '用户ID *',
					name : 'login_id'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '用户名称 *',
					name : 'name'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '初始密码 *',
					inputType: 'password',
					name : 'password'
				}, Ext.create('Ext.form.ComboBox', {
					fieldLabel : '部门 *',
					anchor : '98%',
					editable : false,
					store : Ext.data.StoreManager.lookup('allSectionStore'),
					queryMode : 'local',
					displayField : 'name',
					valueField : 'id',
					name: 'section',
					renderTo : Ext.getBody()
				}),{
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '职位 *',
					name : 'post'
				}, Ext.create('Ext.form.ComboBox', {
					fieldLabel : '所属部门1 *',
					anchor : '98%',
					editable : false,
					store : Ext.data.StoreManager.lookup('allRoleStore'),
					queryMode : 'local',
					displayField : 'name',
					valueField : 'id',
					name: 'role',
					renderTo : Ext.getBody()
				}),Ext.create('Ext.form.ComboBox', {
					fieldLabel : '所属部门2 *',
					anchor : '98%',
					editable : false,
					store : Ext.data.StoreManager.lookup('allRoleStore'),
					queryMode : 'local',
					displayField : 'name',
					valueField : 'id',
					name: 'role2',
					renderTo : Ext.getBody()
				}),{
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '电子邮件',
					name : 'email'
				}, Ext.create('Ext.form.ComboBox', {
					fieldLabel : '性别',
					anchor : '98%',
					editable : false,
					store : new Ext.data.ArrayStore({
						autoDestory : true,
						fields : [ 'sex' ],
						data : [ [ '男' ], [ '女' ] ]
					}),
					value: '男',
					queryMode : 'local',
					displayField : 'sex',
					valueField : 'sex',
					name: 'sex',
					renderTo : Ext.getBody()
				}) 
				,  {
					anchor : '98%',
					fieldLabel : '出生日期',
					editable : false,
					name : 'birthday',
					xtype: 'datefield',
			        maxValue: new Date(),
			        format: 'Y-m-d'
				}, {
					anchor : '98%',
					fieldLabel : '雇用日期',
					editable : false,
					name : 'employ_date',
					xtype: 'datefield',
			        maxValue: new Date(),
			        format: 'Y-m-d'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '工资(元)',
					name : 'salary'
				}, Ext.create('Ext.form.ComboBox', {
					fieldLabel : '状态',
					anchor : '98%',
					editable : false,
					store : new Ext.data.ArrayStore({
						autoDestory : true,
						fields : [ 'status' ],
						data : [ [ '有效' ], [ '无效' ] ]
					}),
					value: '有效',
					queryMode : 'local',
					displayField : 'status',
					valueField : 'status',
					name: 'status',
					renderTo : Ext.getBody()
				})],
				buttons : [ {
					text : '确定',
					handler : function() {
						var form = this.up('form').getForm();
						
						if(Ext.String.trim(this.up('form').down('textfield[name="login_id"]').getValue()) == '' || 
							Ext.String.trim(this.up('form').down('textfield[name="name"]').getValue()) == '' ||
							Ext.String.trim(this.up('form').down('textfield[name="password"]').getValue()) == '' ||
							this.up('form').down('combobox[name="section"]').getValue() == null ||
							Ext.String.trim(this.up('form').down('textfield[name="post"]').getValue()) == '' ||
							this.up('form').down('combobox[name="role"]').getValue() == null
							)
						{
							Ext.Msg.alert('添加结果','带*号资料必须输入');
							return;
						}
						
						form.submit({
							url : 'UserACController?action=addUser',
							success : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allUserACStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allUserACStore').load();
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
	onEditUserButtonClick : function() {
		Ext.Msg.alert("System","Function is developing");
	},
	onDeleteUserButtonClick : function() {
		var win = Ext.create('Ext.window.Window', {
			title : '删除用户',
			height : 60,
			width : 80,
			layout : 'fit',
			modal: true,
			items : [ Ext.create('Ext.form.Panel', {
				layout : 'anchor',
				baseCls: 'x-plain',
				border : false,
				defaults : {
					anchor : '98%'
				},
				items : [
			         {
			        	 xtype : 'box',
			        	 html: '<table><tr height=10><td></td></tr><tr><td>是否删除该用户?</td></tr></table>'
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
            	 		 
            	 		 var selected = Ext.getCmp('user-grid').getSelectionModel().getSelection();
            	 		 if(selected.length == 0)
        	 			 {
            	 			 Ext.Msg.alert("删除用户","请选择要删除的用户");
            	 			 win.close();
            	 			 return;
        	 			 }
            	 		 
            	 		 this.up('form').down('hiddenfield[name="id"]').setValue(selected[0].data.id);
            	 		 
            	 		 this.up('form').getForm().submit({
            	 			url : 'UserACController?action=deleteUser',
							success : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allUserACStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allUserACStore').load();
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
		
		var selected = Ext.getCmp('user-grid').getSelectionModel().getSelection();
		if(selected.length == 0)
		{
			Ext.Msg.alert("删除用户","请选择要删除的用户");
			return;
		}
		else
		{
			win.show();	
		}
	}
});