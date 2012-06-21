Ext.define('inventory.controller.c_inventory', {
    extend: 'Ext.app.Controller',

    views: [
            'v_inventory'
        ],
    
    init: function() {
        this.control({
            'viewport > panel': {
                render: this.onPanelRendered
            },
            'panel[id="inventory-grid"]' :{
            	render: this.onJobGridRendered
            },
            'button[text=出仓]' : {
				click : this.onSellProductClicked
			}
        });
    },

    onPanelRendered: function() {
        
    },
    
    onJobGridRendered: function() {
    	Ext.data.StoreManager.lookup('allProductStore').load();
    },
    
    onSellProductClicked : function() {
    	var hasPermission = HAS_PERMISSION("仓库");
    	if(!hasPermission)
    		return;
    	
    	var win = Ext.create('Ext.window.Window', {
			title : '出仓',
			height : 160,
			width : 400,
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
						xtype : 'textfield',
						readOnly : true,
						name : 'name',
						fieldLabel : '产品'
					 },
			         {
			        	xtype : 'textfield',
			        	name : 'quantity',
			        	fieldLabel : '数量'
			         }
		         ],
		         buttons: [
                 {
            	 	 text : '确定',
            	 	 handler : function(){
            	 		 
            	 		 var selectedProduct = Ext.getCmp('inventory-grid').getSelectionModel().getSelection();
            	 		 if(selectedProduct.length == 0)
        	 			 {
            	 			 Ext.Msg.alert("出仓","请选择要出仓的产品");
            	 			 win.close();
            	 			 return;
        	 			 }
            	 		 
            	 		 if(win.down('textfield[name="quantity"]').getValue() <= 0 ||
            	 				isNaN(win.down('textfield[name="quantity"]').getValue()))
        	 			 {
            	 			 Ext.Msg.alert("出仓","请输入正确的出仓数量");
            	 			 return;
        	 			 }
            	 		 
            	 		 this.up('form').getForm().submit({
            	 			url : 'ProductController?action=sellProduct',
							success : function(form, resp) {
								Ext.Msg.alert('出仓结果', resp.result.msg);
								Ext.data.StoreManager.lookup('allProductStore').load();
								win.close();
							},
							failure : function(form, resp) {
								Ext.Msg.alert('出仓结果', resp.result.msg);
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
		
		var selectedProduct = Ext.getCmp('inventory-grid').getSelectionModel().getSelection();
		if(selectedProduct.length == 0)
		{
			Ext.Msg.alert("出仓","请选择要出仓的产品");
			return;
		}
		else
		{
			win.down('textfield[name="name"]').setValue(selectedProduct[0].data.name);
			win.show();	
		}
    }
});