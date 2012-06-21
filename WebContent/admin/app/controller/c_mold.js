Ext.define('admin.controller.c_mold', {
	extend : 'Ext.app.Controller',

	views : [ 'v_admin' ],

	init : function() {
		this.control({
			'panel[id=mold-grid]' : {
				render : this.onMoldGridRendered
			},
			'button[text=添加模具]' : {
				click : this.onAddMoldClick
			},
			'button[text=删除模具]' : {
				click : this.onDeleteMoldClick
			}
		});
	},
	
	onMoldGridRendered : function() {
		Ext.data.StoreManager.lookup('allMoldStore').load();
	},
	
	onAddMoldClick : function() {
		var win = Ext.create('Ext.window.Window', {
			title : '模具资料',
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
					fieldLabel : '模具号码',
					name : 'code'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '模具名称',
					name : 'name'
				}, {
					xtype : 'textfield',
					anchor : '98%',
					fieldLabel : '所在架号',
					name : 'stand_no'
				}],
				buttons : [ {
					text : '确定',
					handler : function() {
						var form = this.up('form').getForm();
						
						if(Ext.String.trim(this.up('form').down('textfield[name="name"]').getValue()) == '' || 
							Ext.String.trim(this.up('form').down('textfield[name="code"]').getValue()) == ''
							)
						{
							Ext.Msg.alert('添加结果','请输入模具号码与模具名称');
							return;
						}
						
						form.submit({
							url : 'MoldController?action=addMold',
							success : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allMoldStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allMoldStore').load();
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
	
	onDeleteMoldClick : function(){
		var win = Ext.create('Ext.window.Window', {
			title : '删除模具',
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
			        	 html: '<table><tr height=10><td></td></tr><tr><td>是否删除该模具?</td></tr></table>'
			         },
			         {
			        	 xtype : 'hiddenfield',
			        	 name: 'code'
			         }
		         ],
		         buttons: [
                 {
            	 	 text : '确定',
            	 	 handler : function(){
            	 		 
            	 		 var selected = Ext.getCmp('mold-grid').getSelectionModel().getSelection();
            	 		 if(selected.length == 0)
        	 			 {
            	 			 Ext.Msg.alert("删除模具","请选择要删除的模具");
            	 			 win.close();
            	 			 return;
        	 			 }
            	 		 
            	 		 this.up('form').down('hiddenfield[name="code"]').setValue(selected[0].data.code);
            	 		 
            	 		 this.up('form').getForm().submit({
            	 			url : 'MoldController?action=deleteMold',
							success : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allMoldStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allMoldStore').load();
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
		
		var selected = Ext.getCmp('mold-grid').getSelectionModel().getSelection();
		if(selected.length == 0)
		{
			Ext.Msg.alert("删除模具","请选择要删除的模具");
			return;
		}
		else
		{
			win.show();	
		}
	}
});