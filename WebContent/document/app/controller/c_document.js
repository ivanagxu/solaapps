Ext.define('document.controller.c_document', {
	extend : 'Ext.app.Controller',

	views : [ 'v_document' ],


	init : function() {
		this.control({
			'panel[id=document-grid]' : {
				render : this.onDocumentGridRender
			},
			'button[text=添加文档]' : {
				click : this.onAddDocClicked
			},
			'button[text=下载文档]' : {
				click : this.onDownloadDocClicked
			},
			'button[text=删除文档]' : {
				click : this.onDeleteDocClicked
			},
			'button[text=刷新文档]' : {
				click : this.onRefreshDocClicked
			}
		});
	},
	
	onDocumentGridRender : function()
	{
		Ext.data.StoreManager.lookup('documentStore').load();
	},
	
	onAddDocClicked : function()
	{
		var win = Ext.create('Ext.window.Window', {
			title : '添加文档',
			height : 120,
			width : 600,
			layout : 'fit',
			modal: true,
			items : [ Ext.create('Ext.form.Panel', {
				layout : 'anchor',
				border : false,
				containScroll : true,
				autoScroll : true,
				baseCls: 'x-plain',
				defaults : {
					anchor : '98%'
				},
				items : [ {
					xtype : 'filefield',
					name : 'file',
					fieldLabel : '选择文档',
					msgTarget : 'side',
					buttonText : '浏览...'
				} ],
				buttons : [ {
					text : '确定',
					handler : function() {
						var form = this.up('form').getForm();
						var name = Ext.String.trim(this.up('form').down('filefield[name="file"]').getValue()).toUpperCase();
						if(name == '')
						{
							Ext.Msg.alert('添加结果','请选择文件');
							return;
						}
						var type = /[^.]+$/.exec(name);
						if(type != "PDF" && type != "DOC" && type != "DOCX" && type != "XSL" && type != "XLSX" && type != "TXT"
							&& type != "PPT" && type != "PPTX" && type != "JPG" && type != "JPEG" && type != "GIF" &&
							type != "RTF" && type != "BMP" && type != "TIF" && type != "TIFF" && type != "PNG")
						{
							Ext.Msg.alert('添加结果','文件格式受限，请重新选择');
							return;
						}
						
						form.submit({
							url : 'DocumentController?action=uploadDoc',
							success : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('documentStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('添加结果', resp.result.msg);
								Ext.data.StoreManager.lookup('documentStore').load();
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
	
	onDeleteDocClicked : function()
	{
		var win = Ext.create('Ext.window.Window', {
			title : '删除文档',
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
			        	 html: '<table><tr height=10><td></td></tr><tr><td>是否删除该文档?</td></tr></table>'
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
            	 		 
            	 		 var selectedDocument = Ext.getCmp('document-grid').getSelectionModel().getSelection();
            	 		 if(selectedDocument.length == 0)
        	 			 {
            	 			 Ext.Msg.alert("删除文档","请选择要删除的文档");
            	 			 win.close();
            	 			 return;
        	 			 }
            	 		 
            	 		 this.up('form').down('hiddenfield[name="name"]').setValue(selectedDocument[0].data.name);
            	 		 
            	 		 this.up('form').getForm().submit({
            	 			url : 'DocumentController?action=deleteDocument',
							success : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('documentStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('删除结果', resp.result.msg);
								Ext.data.StoreManager.lookup('documentStore').load();
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
		
		var selectedDocument = Ext.getCmp('document-grid').getSelectionModel().getSelection();
		if(selectedDocument.length == 0)
		{
			Ext.Msg.alert("删除文档","请选择要删除的文档");
			return;
		}
		else
		{
			win.show();	
		}
	},
	
	onRefreshDocClicked : function()
	{
		Ext.data.StoreManager.lookup('documentStore').load();
	},
	
	onDownloadDocClicked : function()
	{
		var selectedDocument = Ext.getCmp('document-grid').getSelectionModel().getSelection();
		if(selectedDocument.length == 0)
		{
			Ext.Msg.alert("下载文档","请选择要下载的文档");
			return;
		}
		else
		{
			window.open('download.jsp?name=' + selectedDocument[0].data.name);
		}
	}
	
	
});