Ext.define('admin.controller.c_product', {
	extend : 'Ext.app.Controller',

	views : [ 'v_admin' ],

	init : function() {
		this.control({
			'panel[id=product-grid]' : {
				render : this.onProductGridRendered
			},
			'button[text=添加产品]' : {
				click : this.onAddProductButtonClick
			},
			'button[text=修改产品]' : {
				click : this.onEditProductButtonClick
			},
			'button[text=删除产品]' : {
				click : this.onDeleteProductButtonClick
			}
		});
	},
	
	onProductGridRendered : function() {
		Ext.data.StoreManager.lookup('allProductStore').load();
	},
	onEditProductButtonClick : function() {
		Ext.data.StoreManager.lookup('allMoldStore').load();
		
		var win = Ext.create('Ext.window.Window', {
			title : '产品资料',
			height : 480,
			width : 600,
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
					anchor : '50%',
					fieldLabel : '客户料号',
					name : 'name',
					readOnly: true
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '英文名',
					name : 'name_eng',
					hidden: true
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '料号',
					name : 'our_name'
				}, Ext.create('Ext.form.ComboBox', {
					fieldLabel : '模具号码',
					anchor : '50%',
					editable : false,
					store : Ext.data.StoreManager.lookup('allMoldStore'),
					queryMode : 'local',
					displayField : 'code',
					valueField : 'code',
					name: 'mold_code',
					renderTo : Ext.getBody(),
					listeners: {
						'select' : function(com, rec, idx)
						{
							this.up('form').down('[name=mold_name]').setValue(rec[0].data.code);
						}
					}
				}),Ext.create('Ext.form.ComboBox', {
					fieldLabel : '模具名称',
					anchor : '50%',
					editable : false,
					store : Ext.data.StoreManager.lookup('allMoldStore'),
					queryMode : 'local',
					displayField : 'name',
					valueField : 'code',
					name: 'mold_name',
					renderTo : Ext.getBody(),
					listeners: {
						'select' : function(com, rec, idx)
						{
							this.up('form').down('[name=mold_code]').setValue(rec[0].data.code);
						}
					}
				}),{
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '模具率',
					name : 'mold_rate'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '机加位',
					name : 'machining_pos'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '手工位',
					name : 'handwork_pos'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '抛光难度',
					name : 'polishing'
				}, {
					xtype : 'filefield',
					name : 'image',
					fieldLabel : '产品图片',
					msgTarget : 'side',
					buttonText : '浏览...'
				}, {
					xtype : 'filefield',
					name : 'drawing',
					fieldLabel : '产品图纸',
					msgTarget : 'side',
					buttonText : '浏览...'
				}, Ext.create('Ext.form.ComboBox', {
					fieldLabel : '状态',
					anchor : '30%',
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
				}),{
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '已有半成品',
					name : 'semi_finished'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '已有成品',
					name : 'finished'
				}],
				buttons : [ {
					text : '确定',
					handler : function() {
						var form = this.up('form').getForm();
						
						if(Ext.String.trim(this.up('form').down('textfield[name="name"]').getValue()) == '' || 
							Ext.String.trim(this.up('form').down('textfield[name="our_name"]').getValue()) == ''
							)
						{
							Ext.Msg.alert('修改结果','请输入产品名称');
							return;
						}
						
						var fileImage = this.up('form').down('filefield[name="image"]').getValue().toUpperCase();
						var fileDrawing = this.up('form').down('filefield[name="drawing"]').getValue().toUpperCase();
						
						if(fileImage != "")
						{
							var typeImage = /[^.]+$/.exec(fileImage);
							if(typeImage != "JPG" && typeImage != "JPEG" && typeImage != "GIF" &&
									typeImage != "BMP" && typeImage != "TIF" && typeImage != "TIFF" && typeImage != "PNG")
							{
								Ext.Msg.alert('添加结果','文件格式受限，请重新选择');
								return;
							}
						}
						
						if(fileDrawing != "")
						{
							var typeDrawing = /[^.]+$/.exec(fileDrawing);
							if(typeDrawing != "JPG" && typeDrawing != "JPEG" && typeDrawing != "GIF" &&
									typeDrawing != "BMP" && typeDrawing != "TIF" && typeDrawing != "TIFF" && typeDrawing != "PNG")
							{
								Ext.Msg.alert('添加结果','文件格式受限，请重新选择');
								return;
							}
						}
						form.submit({
							url : 'ProductController?action=updateProduct',
							success : function(form, resp) {
								Ext.Msg.alert('修改结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allProductStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('修改结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allProductStore').load();
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
		
		var selectedProduct = Ext.getCmp('product-grid').getSelectionModel().getSelection();
		if(selectedProduct.length == 0)
		{
			Ext.Msg.alert("修改产品","请选择要修改的产品");
			return;
		}
		else
		{
			function fillProductForm(data)
			{
				if(!data)
				{
					Ext.Msg.alert("修改产品", "获取产品详细资料失败");
				}
				else
				{
					win.down('textfield[name="name"]').setValue(data.name);
					win.down('textfield[name="name_eng"]').setValue(data.name_eng);
					win.down('combobox[name="status"]').setValue(data.status);
					win.down('textfield[name="image"]').setValue(data.image);
					win.down('textfield[name="drawing"]').setValue(data.drawing);
					win.down('textfield[name="our_name"]').setValue(data.our_name);
					win.down('textfield[name="mold_rate"]').setValue(data.mold_rate);
					win.down('textfield[name="machining_pos"]').setValue(data.machining_pos);
					win.down('textfield[name="handwork_pos"]').setValue(data.handwork_pos);
					win.down('textfield[name="polishing"]').setValue(data.polishing);
					win.down('textfield[name="finished"]').setValue(data.finished);
					win.down('textfield[name="semi_finished"]').setValue(data.semi_finished);
					win.down('textfield[name="mold_code"]').setValue(data.mold_code);
					win.down('textfield[name="mold_name"]').setValue(data.mold_code);
				}
				win.show();
			}	
			GetJsonData("ProductController?action=getProductByName",{name: selectedProduct[0].data.name}, fillProductForm);
			
		}
	},
	onDeleteProductButtonClick : function(){
		var win = Ext.create('Ext.window.Window', {
			title : '删除产品',
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
			        	 html: '<table><tr height=10><td></td></tr><tr><td>是否删除该产品?</td></tr></table>'
			         },
			         {
			        	 xtype : 'hiddenfield',
			        	 name: 'name'
			         }
		         ],
		         buttons: [
                 {
            	 	 text : '确定',
            	 	 handler : function(){
            	 		 
            	 		 var selectedProduct = Ext.getCmp('product-grid').getSelectionModel().getSelection();
            	 		 if(selectedProduct.length == 0)
        	 			 {
            	 			 Ext.Msg.alert("删除产品","请选择要删除的产品");
            	 			 win.close();
            	 			 return;
        	 			 }
            	 		 
            	 		 this.up('form').down('hiddenfield[name="name"]').setValue(selectedProduct[0].data.name);
            	 		 
            	 		 this.up('form').getForm().submit({
            	 			url : 'ProductController?action=deleteProduct',
							success : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allProductStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allProductStore').load();
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
		
		var selectedProduct = Ext.getCmp('product-grid').getSelectionModel().getSelection();
		if(selectedProduct.length == 0)
		{
			Ext.Msg.alert("删除产品","请选择要删除的产品");
			return;
		}
		else
		{
			win.show();	
		}
	},
	
	onAddProductButtonClick : function() {
		Ext.data.StoreManager.lookup('allMoldStore').load();
		var win = Ext.create('Ext.window.Window', {
			title : '产品资料',
			height : 480,
			width : 600,
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
					anchor : '50%',
					fieldLabel : '客户料号',
					name : 'name'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '英文名',
					name : 'name_eng',
					hidden: true
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '料号',
					name : 'our_name'
				}, Ext.create('Ext.form.ComboBox', {
					fieldLabel : '模具号码',
					anchor : '50%',
					editable : false,
					store : Ext.data.StoreManager.lookup('allMoldStore'),
					queryMode : 'local',
					displayField : 'code',
					valueField : 'code',
					name: 'mold_code',
					renderTo : Ext.getBody(),
					listeners: {
						'select' : function(com, rec, idx)
						{
							this.up('form').down('[name=mold_name]').setValue(rec[0].data.code);
						}
					}
				}),Ext.create('Ext.form.ComboBox', {
					fieldLabel : '模具名称',
					anchor : '50%',
					editable : false,
					store : Ext.data.StoreManager.lookup('allMoldStore'),
					queryMode : 'local',
					displayField : 'name',
					valueField : 'code',
					name: 'mold_name',
					renderTo : Ext.getBody(),
					listeners: {
						'select' : function(com, rec, idx)
						{
							this.up('form').down('[name=mold_code]').setValue(rec[0].data.code);
						}
					}
				}),{
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '模具率',
					name : 'mold_rate'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '机加位',
					name : 'machining_pos'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '手工位',
					name : 'handwork_pos'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '抛光难度',
					name : 'polishing'
				}, {
					xtype : 'filefield',
					name : 'image',
					fieldLabel : '产品图片',
					msgTarget : 'side',
					buttonText : '浏览...'
				}, {
					xtype : 'filefield',
					name : 'drawing',
					fieldLabel : '产品图纸',
					msgTarget : 'side',
					buttonText : '浏览...'
				}, Ext.create('Ext.form.ComboBox', {
					fieldLabel : '状态',
					anchor : '30%',
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
				}),{
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '已有半成品',
					name : 'semi_finished'
				}, {
					xtype : 'textfield',
					anchor : '50%',
					fieldLabel : '已有成品',
					name : 'finished'
				}],
				buttons : [ {
					text : '确定',
					handler : function() {
						var form = this.up('form').getForm();
						
						if(Ext.String.trim(this.up('form').down('textfield[name="name"]').getValue()) == '' || 
							Ext.String.trim(this.up('form').down('textfield[name="our_name"]').getValue()) == ''
							)
						{
							Ext.Msg.alert('添加结果','请输入产品名称');
							return;
						}
						
						var fileImage = this.up('form').down('filefield[name="image"]').getValue().toUpperCase();
						var fileDrawing = this.up('form').down('filefield[name="drawing"]').getValue().toUpperCase();
						
						if(fileImage != "")
						{
							var typeImage = /[^.]+$/.exec(fileImage);
							if(typeImage != "JPG" && typeImage != "JPEG" && typeImage != "GIF" &&
									typeImage != "BMP" && typeImage != "TIF" && typeImage != "TIFF" && typeImage != "PNG")
							{
								Ext.Msg.alert('添加结果','文件格式受限，请重新选择');
								return;
							}
						}
						
						if(fileDrawing != "")
						{
							var typeDrawing = /[^.]+$/.exec(fileDrawing);
							if(typeDrawing != "JPG" && typeDrawing != "JPEG" && typeDrawing != "GIF" &&
									typeDrawing != "BMP" && typeDrawing != "TIF" && typeDrawing != "TIFF" && typeDrawing != "PNG")
							{
								Ext.Msg.alert('添加结果','文件格式受限，请重新选择');
								return;
							}
						}
						
						/*
						if(this.up('form').down('filefield[name="image"]').getValue().toUpperCase().indexOf('.JPG') <=0 ||
							this.up('form').down('filefield[name="drawing"]').getValue().toUpperCase().indexOf('.JPG') <=0
						)
						{
							Ext.Msg.alert('添加结果','请选择JPG格式的图片');
							return;
						}
						*/
						
						form.submit({
							url : 'ProductController?action=addProduct',
							success : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allProductStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allProductStore').load();
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
	}
});