Ext.define('order.controller.c_order', {
    extend: 'Ext.app.Controller',

    views: [
            'v_order'
        ],
    
    init: function() {
        
        this.control({
            'viewport > panel': {
                render: this.onPanelRendered
            },
            'panel[id="order-grid"]' :{
                render: this.onOrderGridRendered
            },
            'button[text="创建订单"]':{
                click: this.onCreateOrderClicked
            },
            'button[text="审核订单"]':{
                click: this.onApproveOrderClicked
            },
            'button[text="查看订单"]':{
                click: this.onUpdateOrderClicked
            },
            'button[text="删除订单"]':{
                click: this.onDeleteOrderClicked
            },
            'button[text="取消订单"]':{
                click: this.onCancelOrderClicked
            },
            'button[text="暂停订单"]':{
                click: this.onPauseOrderClicked
            },
            'button[text="恢复订单"]' : {
                click : this.onResumeOrderClicked
            },
            'menuitem[text="生产总数"]' : {
                click : this.onSummaryTotalClicked
            },
            'menuitem[text="订单总数"]' : {
                click : this.onSummaryOrderTotalClicked
            }
        });
        
    },

    createOrderWin : function() {
        var win = Ext.create('Ext.window.Window', {
                        id : 'order-win',
                        title : '订单资料',
                        height : 500,
                        width : 800,
                        layout : 'fit',
                        autoDestory : true,
                        modal: true,
                        items : [ Ext.create('Ext.form.Panel', {
                                layout : 'anchor',
                                baseCls: 'x-plain',
                                border : false,
                                containScroll: true,
                                autoScroll: true,
                                defaults : {
                                        anchor : '98%'
                                },
                                items : [ 
                                {
                                        xtype : 'fieldset',
                                        title : '基本资料',
                                        defaultType : 'textfield',
                                        layout : 'anchor',
                                        defaults : {
                                                anchor : '100%'
                                        },
                                        items:[
                                                {
                                                        xtype : 'hidden',
                                                        name : 'orderid'
                                                }, 
                                                {
                                                        fieldLabel : '订单号',
                                                        name : 'ordernumber',
                                                        readOnly : true,
                                                        hidden: true
                                                }, Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '跟单人员 *',
                                                        editable : false,
                                                        store : Ext.data.StoreManager.lookup('allUserACStore'),
                                                        queryMode : 'local',
                                                        displayField : 'name',
                                                        valueField : 'id',
                                                        name : 'creator',
                                                        readOnly : true,
                                                        renderTo : Ext.getBody()
                                                }), Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '客户名称 *',
                                                        editable : false,
                                                        store : Ext.data.StoreManager.lookup('allCustomerStore'),
                                                        queryMode : 'local',
                                                        displayField : 'name',
                                                        valueField : 'id',
                                                        name : 'customer_name',
                                                        renderTo : Ext.getBody(),
                                                        listeners: {
                                                                'select' : function(com, rec, idx)
                                                                {
                                                                        this.up('form').down('[name=customer_code]').setValue(rec[0].data.id);
                                                                }
                                                        }
                                                }), Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '客户代码 *',
                                                        editable : false,
                                                        store : Ext.data.StoreManager.lookup('allCustomerStore'),
                                                        queryMode : 'local',
                                                        displayField : 'code',
                                                        valueField : 'id',
                                                        name : 'customer_code',
                                                        renderTo : Ext.getBody(),
                                                        listeners: {
                                                                'select' : function(com, rec, idx)
                                                                {
                                                                        this.up('form').down('[name=customer_name]').setValue(rec[0].data.id);
                                                                }
                                                        }
                                                }), Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '客户料号 *',
                                                        editable : false,
                                                        store : Ext.data.StoreManager.lookup('productStore'),
                                                        queryMode : 'local',
                                                        displayField : 'name',
                                                        valueField : 'name',
                                                        name : 'product_name',
                                                        renderTo : Ext.getBody(),
                                                        listeners: {
                                                                'select' : function(com, rec, idx)
                                                                {
                                                                        this.up('form').down('[name=product_our_name]').setValue(rec[0].data.our_name);
                                                                }
                                                        }
                                                }),
                                                {
                                                        fieldLabel : '料号 *',
                                                        name : 'product_our_name',
                                                        readOnly: true
                                                }, {
                                           xtype : 'textfield',
                                           name : 'e_quantity',
                                           fieldLabel : '订单数量',
                                           width : 160,
                                           listeners: {
                                                   change : function(){
                                                        win.down('textfield[name="use_finished"]').setValue(0);
                                                                        win.down('textfield[name="use_semi_finished"]').setValue(0);
                                                   }
                                           }
                                    }, {
                                                        anchor : '98%',
                                                        fieldLabel : '交货日期 *',
                                                        editable : false,
                                                        name : 'c_deadline',
                                                        xtype: 'datefield',
                                                format: 'Y-m-d'
                                                }, {
                                                        xtype : 'container',
                                                        layout : 'column',
                                                        items : [ {
                                                                xtype : 'textfield',
                                                                fieldLabel : '使用成品个数',
                                                                name : 'use_finished'
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '使用半成品数',
                                                                name : 'use_semi_finished'
                                                        }, {
                                                                xtype : 'box',
                                                                html : '&nbsp;&nbsp;'
                                                        },{
                                                                xtype : 'button',
                                                                text : '查看库存',
                                                                handler : function()
                                                                {
                                                                        var eq = win.down('textfield[name="e_quantity"]').getValue();
                                                                        function fillUseProductCount(data)
                                                                        {
                                                                                if(eq <= data.finished)
                                                                                {
                                                                                        win.down('textfield[name="use_finished"]').setValue(eq);
                                                                                }
                                                                                else if(eq > data.finished && eq <= data.finished + data.semi_finished)
                                                                                {
                                                                                        win.down('textfield[name="use_finished"]').setValue(data.finished);
                                                                                        win.down('textfield[name="use_semi_finished"]').setValue(data.semi_finished);
                                                                                }
                                                                                else if(eq > data.finished + data.semi_finished)
                                                                                {
                                                                                        win.down('textfield[name="use_finished"]').setValue(data.finished);
                                                                                        win.down('textfield[name="use_semi_finished"]').setValue(data.semi_finished);
                                                                                }
                                                                        }
                                                                        
                                                                        var product_name = win.down('combobox[name="product_name"]').getValue();
                                                                        if(product_name == "" || product_name == null)
                                                                        {
                                                                                Ext.Msg.alert("创建订单","请先选择产品");
                                                                                return;
                                                                        }
                                                                        
                                                                        if(isNaN(eq) || eq < 0 || eq == "")
                                                                        {
                                                                                Ext.Msg.alert("创建订单", "请先输入正确的订单数量");
                                                                                return;
                                                                        }
                                                                        
                                                                        GetJsonData("ProductController?action=getProductByName",{name: product_name}, fillUseProductCount);
                                                                }
                                                        }
                                                        ]
                                                }, {
                                                        xtype : 'textareafield',
                                                        grow : true,
                                                        name : 'requirement1',
                                                        fieldLabel : '电镀要求'
                                                }, {
                                                        xtype : 'textareafield',
                                                        grow : true,
                                                        name : 'requirement2',
                                                        fieldLabel : '特殊要求'
                                                }, {
                                                        anchor : '10%',
                                                        xtype: 'button',
                                                        text : '查看成品率',
                                                        handler: function(){
                                                                var product_name = win.down('combobox[name="product_name"]').getValue();
                                                                if(product_name == "" || product_name == null)
                                                                {
                                                                        Ext.Msg.alert("创建订单","请先选择产品");
                                                                        return;
                                                                }
                                                                
                                                                Ext.data.StoreManager.lookup('productRateStore').proxy.url = 
                                                                        'ReportController?action=generateProductRateReportByProduct&product_name=' + product_name;
                                                                Ext.data.StoreManager.lookup('productRateStore').load();
                                                                
                                                                var productRateWin = Ext.create('Ext.window.Window', {
                                                                        id : 'product-rate-win',
                                                                        title : '产品成品率',
                                                                        height : 320,
                                                                        width : 480,
                                                                        layout : 'fit',
                                                                        autoDestory : true,
                                                                        modal: true,
                                                                        items: [
                                                                                Ext.create('Ext.form.Panel', {
                                                                                        layout : 'anchor',
                                                                                        border : false,
                                                                                        containScroll: true,
                                                                                        autoScroll: true,
                                                                                        items : [Ext.create('Ext.grid.Panel', {
                                                                                                id : 'product-rate-grid',
                                                                                                store : Ext.data.StoreManager.lookup('productRateStore'),
                                                                                                columns : [ {
                                                                                                        header : '订单号',
                                                                                                        dataIndex : 'order_number'
                                                                                                },{
                                                                                                        header : '料号',
                                                                                                        dataIndex : 'product_our_name'
                                                                                                }, {
                                                                                                        header : '成品率',
                                                                                                        dataIndex : 'rate'
                                                                                                },{
                                                                                                        header : '备注',
                                                                                                        dataIndex : 'remark'
                                                                                                }],
                                                                                                height : 280,
                                                                                                renderTo : Ext.getBody()
                                                                                        }) ]
                                                                                })
                                                                ]
                                                                });
                                                                
                                                                productRateWin.show();
                                                        }
                                                }, {
                                                        anchor : '98%',
                                                        fieldLabel : '生产期限',
                                                        editable : false,
                                                        name : 'deadline',
                                                        xtype: 'datefield',
                                                format: 'Y-m-d'
                                                }, {
                                                        xtype : 'container',
                                                        layout : 'column',
                                                        items:[
                                                               {
                                                                        xtype : 'container',
                                                                        columnWidth : 0.3,
                                                                        items:[
                                                                       {
                                                                           xtype : 'textfield',
                                                                           name : 'product_rate',
                                                                           fieldLabel : '预成率',
                                                                           width : 160,
                                                                           listeners:{
                                                                                   change: function(e, v)
                                                                                   {
                                                                                           var usefinish = win.down('textfield[name="use_finished"]').getValue();
                                                                                           var usesemifinish = win.down('textfield[name="use_semi_finished"]').getValue();
                                                                                           if(isNaN(usefinish))
                                                                                                   usefinish = 0;
                                                                                           if(isNaN(usesemifinish))
                                                                                                   usesemifinish = 0;
                                                                                           
                                                                                           var eq = e.up('form').down('textfield[name="e_quantity"]').getValue();
                                                                                           if(isNaN(eq) || isNaN(v) || v == 0 || eq == 0)
                                                                                           {
                                                                                                   e.up('form').down('textfield[name="quantity"]').setValue(0);
                                                                                           }
                                                                                           else
                                                                                           {
                                                                                                   //生产数量（自动生成）=（订单数量-使用成品数）/成品率 - 半成品数
                                                                                                   var pq = Math.ceil((eq - usefinish) / v - usesemifinish);
                                                                                                   if(pq < 0) pq = 0;
                                                                                                   e.up('form').down('textfield[name="quantity"]').setValue(pq);
                                                                                           }
                                                                                           win.down('textfield[name="quantity1"]').setValue(
                                                                                                           win.down('textfield[name="quantity"]').getValue()
                                                                                                   );
                                                                                   }
                                                                           }
                                                                       }
                                                               ]
                                                               },
                                                               {
                                                                        xtype : 'container',
                                                                        columnWidth : 0.4,
                                                                        items:[
                                                                       {
                                                                           xtype : 'textfield',
                                                                           name : 'quantity',
                                                                           readOnly : true,
                                                                           labelWidth : 120,
                                                                           fieldLabel : '生产数量(自动生成)'
                                                                       }
                                                               ]
                                                               }
                                                       ]
                                                }, Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '优先级',
                                                        editable : false,
                                                        store : new Ext.data.ArrayStore({
                                                                autoDestory : true,
                                                                fields : [ 'name', 'value' ],
                                                                data : [ [ '紧急','1' ], [ '普通' , '0'] ]
                                                        }),
                                                        queryMode : 'local',
                                                        displayField : 'name',
                                                        valueField : 'value',
                                                        value : '0',
                                                        name : 'priority'
                                                }),{
                                                        xtype : 'textareafield',
                                                        grow : true,
                                                        name : 'requirement3',
                                                        fieldLabel : '其他要求',
                                                        hidden: true
                                                }, {
                                                        xtype : 'textareafield',
                                                        grow : true,
                                                        name : 'requirement4',
                                                        fieldLabel : '备注'
                                                }
                                ]
                                },         
                                {
                                        xtype : 'fieldset',
                                        title : '部门分配',
                                        items : [
                                                {
                                                        xtype: 'container',
                                                        layout: 'column',
                                                        items:[
                                                               {
                                                                   xtype: 'container',
                                                                   columnWidth: 0.5,
                                                                   items:[
                                                                                        Ext.create('Ext.form.ComboBox', {
                                                                                                fieldLabel : '送交部门',
                                                                                                editable : false,
                                                                                                store : Ext.data.StoreManager.lookup('jobTypeStore'),
                                                                                                queryMode : 'local',
                                                                                                displayField : 'name',
                                                                                                valueField : 'name',
                                                                                                //value : '电镀',
                                                                                                name: 'job_type1',
                                                                                                renderTo : Ext.getBody()
                                                                                        })
                                                                  ]
                                                               },
                                                               {
                                                                   xtype: 'container',
                                                                   columnWidth: 0.5,
                                                                   items:[
                                                                                        {
                                                                                           xtype: 'textfield',
                                                                                           fieldLabel: '数量',
                                                                                           name : 'quantity1'
                                                                                        }
                                                                  ]
                                                               }
                                                    ]
                                                },
                                                {
                                                        xtype: 'container',
                                                        layout: 'column',
                                                        items:[
                                                               {
                                                                   xtype: 'container',
                                                                   columnWidth: 0.5,
                                                                   items:[
                                                                                        Ext.create('Ext.form.ComboBox', {
                                                                                            fieldLabel: '送交部门',
                                                                                            editable: false,
                                                                                            store: Ext.data.StoreManager.lookup('jobTypeStore'),
                                                                                            queryMode: 'local',
                                                                                            displayField: 'name',
                                                                                            valueField: 'name',
                                                                                            name : 'job_type2',
                                                                                            renderTo: Ext.getBody()
                                                                                        })
                                                                  ]
                                                               },
                                                               {
                                                                   xtype: 'container',
                                                                   columnWidth: 0.5,
                                                                   items:[
                                                                                        {
                                                                                           xtype: 'textfield',
                                                                                           fieldLabel: '数量',
                                                                                           name : 'quantity2'
                                                                                        }
                                                                  ]
                                                               }
                                                    ]
                                                },
                                                {
                                                        xtype: 'container',
                                                        layout: 'column',
                                                        items:[
                                                               {
                                                                   xtype: 'container',
                                                                   columnWidth: 0.5,
                                                                   items:[
                                                                                        Ext.create('Ext.form.ComboBox', {
                                                                                            fieldLabel: '送交部门',
                                                                                            editable: false,
                                                                                            store: Ext.data.StoreManager.lookup('jobTypeStore'),
                                                                                            queryMode: 'local',
                                                                                            displayField: 'name',
                                                                                            valueField: 'name',
                                                                                            name : 'job_type3',
                                                                                            renderTo: Ext.getBody()
                                                                                        })
                                                                  ]
                                                               },
                                                               {
                                                                   xtype: 'container',
                                                                   columnWidth: 0.5,
                                                                   items:[
                                                                                        {
                                                                                           xtype: 'textfield',
                                                                                           fieldLabel: '数量',
                                                                                           name : 'quantity3'
                                                                                        }
                                                                  ]
                                                               }
                                                    ]
                                                }
                                        ]
                                }

                                ],
                                buttons : [ {
                                        text : '提交',
                                        handler : function() {
                                                var form = this.up('form').getForm();
                                                if(this.up('form').down('combobox[name="creator"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="customer_name"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="customer_code"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="product_name"]').getValue() == null ||
                                                                this.up('form').down('datefield[name="c_deadline"]').getValue() == null
                                                        )
                                                {
                                                        Ext.Msg.alert("添加订单","带*号资料必须输入");
                                                        return;
                                                }

                                                form.submit({
                                                        url : 'OrderController?action=preCreateOrder',
                                                        success : function(form, action) {
                                                                Ext.Msg.alert('添加结果', action.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                win.close();
                                                        },
                                                        failure : function(form, action) {
                                                                Ext.Msg.alert('添加结果', action.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                        }
                                                });
                                                
                                        }
                                } ,{
                                        text : '审核',
                                        hidden : true,
                                        handler : function() {
                                                var form = this.up('form').getForm();
                                                if(this.up('form').down('combobox[name="creator"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="customer_name"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="customer_code"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="product_name"]').getValue() == null
                                                        )
                                                {
                                                        Ext.Msg.alert("添加订单","带*号资料必须输入");
                                                        return;
                                                }

                                                form.submit({
                                                        url : 'OrderController?action=createOrder',
                                                        success : function(form, action) {
                                                                Ext.Msg.alert('添加结果', action.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                win.close();
                                                        },
                                                        failure : function(form, action) {
                                                                Ext.Msg.alert('添加结果', action.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                        }
                                                });
                                                
                                        }
                                }]
                        }) ]
                });
        
        return win;
    },
    
    onPanelRendered: function() {
        Ext.data.StoreManager.lookup('productStore').load();
        Ext.data.StoreManager.lookup('jobTypeStore').load();
        Ext.data.StoreManager.lookup('allCustomerStore').load();
        Ext.data.StoreManager.lookup('allUserACStore').load();
    },
    
    onOrderGridRendered: function() {
        Ext.data.StoreManager.lookup('orderStore').load();
        Ext.data.StoreManager.lookup('completedOrderStore').load();
    },
    
    onCreateOrderClicked: function(){
        var win = this.createOrderWin();
        win.down('button[text="提交"]').show();
        win.down('button[text="审核"]').hide();
        
                
                win.down('textfield[name="deadline"]').readOnly = true;
                win.down('textfield[name="deadline"]').disable();
                win.down('combobox[name="priority"]').readOnly = true;
                win.down('combobox[name="priority"]').disable();
                win.down('textfield[name="requirement4"]').readOnly = true;
                win.down('textfield[name="requirement4"]').disable();
                win.down('textfield[name="product_rate"]').readOnly = true;
                win.down('textfield[name="product_rate"]').disable();
                win.down('textfield[name="quantity"]').readOnly = true;
                win.down('textfield[name="quantity"]').disable();
                win.down('combobox[name="job_type1"]').readOnly = true;
                win.down('combobox[name="job_type1"]').disable();
                win.down('textfield[name="quantity1"]').readOnly = true;
                win.down('textfield[name="quantity1"]').disable();
                win.down('combobox[name="job_type2"]').readOnly = true;
                win.down('combobox[name="job_type2"]').disable();
                win.down('textfield[name="quantity2"]').readOnly = true;
                win.down('textfield[name="quantity2"]').disable();
                win.down('combobox[name="job_type3"]').readOnly = true;
                win.down('combobox[name="job_type3"]').disable();
                win.down('textfield[name="quantity3"]').readOnly = true;
                win.down('textfield[name="quantity3"]').disable();
        
        win.show();
        win.down('combobox[name=creator]').setValue(SESSION_USER.id);
    },
    onApproveOrderClicked : function()
    {
        var selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                if(selected.length == 0)
                {
                        Ext.Msg.alert("审核订单","请选择要生产审核的订单");
                        return;
                }
                
                var win = this.createOrderWin();
        win.down('button[text="提交"]').hide();
        win.down('button[text="审核"]').show();
        
        function fillOrderForm(data)
        {
                if(data.data.length > 0)
                        {
                        if(data.data[0].status != "审核中")
                                {
                                Ext.Msg.alert("审核订单", "订单已无须审核");
                                Ext.data.StoreManager.lookup('orderStore').load();
                                win.close();
                                return;
                                }
                        
                                win.down('hiddenfield[name="orderid"]').setValue(data.data[0].id);
                                win.down('textfield[name="ordernumber"]').setValue(data.data[0].number);
                                win.down('textfield[name="ordernumber"]').readOnly = true;
                                win.down('textfield[name="ordernumber"]').disable();
                                win.down('textfield[name="e_quantity"]').setValue(data.data[0].e_quantity);
                                win.down('textfield[name="e_quantity"]').readOnly = true;
                                win.down('textfield[name="e_quantity"]').disable();
                                win.down('textfield[name="creator"]').setRawValue(data.data[0].creator);
                                win.down('textfield[name="creator"]').readOnly = true;
                                win.down('textfield[name="creator"]').disable();
                                win.down('textfield[name="customer_code"]').setRawValue(data.data[0].customer_code);
                                win.down('textfield[name="customer_code"]').readOnly = true;
                                win.down('textfield[name="customer_code"]').disable();
                                win.down('textfield[name="customer_name"]').setRawValue(data.data[0].customer_name);
                                win.down('textfield[name="customer_name"]').readOnly = true;
                                win.down('textfield[name="customer_name"]').disable();
                                win.down('textfield[name="customer_name"]').hide();
                                win.down('textfield[name="product_name"]').setValue(data.data[0].product_name);
                                win.down('textfield[name="product_name"]').readOnly = true;
                                win.down('textfield[name="product_name"]').disable();
                                win.down('textfield[name="product_name"]').hide();
                                win.down('textfield[name="product_our_name"]').setValue(data.data[0].product_our_name);
                                win.down('textfield[name="product_our_name"]').readOnly = true;
                                win.down('textfield[name="product_our_name"]').disable();
                                win.down('textfield[name="use_finished"]').setValue(data.data[0].use_finished);
                                win.down('textfield[name="use_finished"]').readOnly = true;
                                win.down('textfield[name="use_finished"]').disable();
                                win.down('textfield[name="use_semi_finished"]').setValue(data.data[0].use_semi_finished);
                                win.down('textfield[name="use_semi_finished"]').readOnly = true;
                                win.down('textfield[name="use_semi_finished"]').disable();
                                win.down('textfield[name="deadline"]').setValue(data.data[0].deadline);
                                win.down('textfield[name="c_deadline"]').setValue(data.data[0].c_deadline);
                                win.down('textfield[name="c_deadline"]').readOnly = true;
                                win.down('textfield[name="c_deadline"]').disable();
                                win.down('combobox[name="priority"]').setRawValue(data.data[0].priority == 1 ? "紧急" : "普通");
                                win.down('textfield[name="requirement1"]').setValue(data.data[0].requirement_1);
                                win.down('textfield[name="requirement1"]').readOnly = true;
                                win.down('textfield[name="requirement1"]').disable();
                                win.down('textfield[name="requirement2"]').setValue(data.data[0].requirement_2);
                                win.down('textfield[name="requirement2"]').readOnly = true;
                                win.down('textfield[name="requirement2"]').disable();
                                win.down('textfield[name="requirement4"]').setValue(data.data[0].requirement_4);
                                win.down('textfield[name="product_rate"]').setValue(data.data[0].product_rate);
                                win.down('textfield[name="quantity"]').setValue(data.data[0].quantity);
                                win.down('button[text="查看库存"]').disable();
                                
                        }
                else
                        {
                        Ext.Msg.alert("审核订单", "获取订单详细资料失败");
                        return;
                        }
        }
                function fillJobForm(data)
                {
                        if(data.data.length > 0)
                        {
                                win.down('combobox[name="job_type1"]').setValue(data.data[0].section);
                                win.down('textfield[name="quantity1"]').setValue(data.data[0].remaining);
                        }
                        else
                        {
                                win.down('combobox[name="job_type1"]').setValue("压铸");
                                win.down('textfield[name="quantity1"]').setValue(
                                        win.down('textfield[name="quantity"]').getValue()
                                );
                        }
                        
                        if(data.data.length > 1)
                        {
                                win.down('combobox[name="job_type2"]').setValue(data.data[1].section);
                                win.down('textfield[name="quantity2"]').setValue(data.data[1].remaining);
                        }
                        
                        if(data.data.length > 2)
                        {
                                win.down('combobox[name="job_type3"]').setValue(data.data[2].section);
                                win.down('textfield[name="quantity3"]').setValue(data.data[2].remaining);
                        }
                }
                GetJsonData("OrderController?action=getOrderById",{id: selected[0].data.id}, fillOrderForm);
                GetJsonData("JobController?action=getJobByOrder",{orderid: selected[0].data.id}, fillJobForm);
                
        win.show();
    },
    
    onUpdateOrderClicked: function(){
        var selected;
        if(Ext.getCmp('order-tab').getActiveTab().title == "在线订单")
                {
                selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                }
        else
                {
                selected = Ext.getCmp('completed-order-grid').getSelectionModel().getSelection();
                }
        
                if(selected.length == 0)
                {
                        Ext.Msg.alert("查看订单","请选择要查看的订单");
                        return;
                }
        
        var win = Ext.create('Ext.window.Window', {
                        id : 'order-win',
                        title : '订单资料',
                        height : 500,
                        width : 800,
                        layout : 'fit',
                        autoDestory : true,
                        modal: true,
                        items : [ Ext.create('Ext.form.Panel', {
                                layout : 'anchor',
                                baseCls: 'x-plain',
                                border : false,
                                containScroll: true,
                                autoScroll: true,
                                defaults : {
                                        anchor : '98%'
                                },
                                items : [ 
                                {
                                        xtype : 'fieldset',
                                        title : '基本资料',
                                        defaultType : 'textfield',
                                        layout : 'anchor',
                                        defaults : {
                                                anchor : '100%'
                                        },
                                        items:[
                                                {
                                                        xtype : 'hidden',
                                                        name : 'orderid'
                                                }, {
                                                        fieldLabel : '订单号',
                                                        name : 'ordernumber',
                                                        readOnly : true
                                                }, Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '跟单人员 *',
                                                        editable : false,
                                                        store : Ext.data.StoreManager.lookup('allUserACStore'),
                                                        queryMode : 'local',
                                                        displayField : 'name',
                                                        valueField : 'name',
                                                        name : 'creator',
                                                        readOnly : true,
                                                        renderTo : Ext.getBody()
                                                }), Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '客户名称 *',
                                                        editable : false,
                                                        store : Ext.data.StoreManager.lookup('allCustomerStore'),
                                                        queryMode : 'local',
                                                        displayField : 'name',
                                                        valueField : 'name',
                                                        name : 'customer_name',
                                                        renderTo : Ext.getBody(),
                                                        readOnly : true,
                                                        listeners: {
                                                                'select' : function(com, rec, idx)
                                                                {
                                                                        this.up('form').down('[name=customer_code]').setValue(rec[0].data.code);
                                                                }
                                                        },
                                                        hidden : location.href.indexOf("production.jsp") >= 0
                                                }), Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '客户代码 *',
                                                        editable : false,
                                                        store : Ext.data.StoreManager.lookup('allCustomerStore'),
                                                        queryMode : 'local',
                                                        displayField : 'code',
                                                        valueField : 'code',
                                                        name : 'customer_code',
                                                        renderTo : Ext.getBody(),
                                                        readOnly : true,
                                                        listeners: {
                                                                'select' : function(com, rec, idx)
                                                                {
                                                                        this.up('form').down('[name=customer_name]').setValue(rec[0].data.name);
                                                                }
                                                        }
                                                }), Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '客户料号 *',
                                                        editable : false,
                                                        store : Ext.data.StoreManager.lookup('productStore'),
                                                        queryMode : 'local',
                                                        displayField : 'name',
                                                        valueField : 'name',
                                                        name : 'product_name',
                                                        renderTo : Ext.getBody(),
                                                        readOnly : true,
                                                        listeners: {
                                                                'select' : function(com, rec, idx)
                                                                {
                                                                        this.up('form').down('[name=product_our_name]').setValue(rec[0].data.our_name);
                                                                }
                                                        },
                                                        hidden : location.href.indexOf("production.jsp") >= 0
                                                }),
                                                {
                                                        fieldLabel : '料号 *',
                                                        name : 'product_our_name',
                                                        readOnly: true
                                                }, {
                                           xtype : 'textfield',
                                           name : 'e_quantity',
                                           fieldLabel : '订单数量',
                                           readOnly : true,
                                           width : 160,
                                           listeners:{
                                                                   change: function(e, v)
                                                                   {
                                                                           var usefinish = win.down('textfield[name="use_finished"]').getValue();
                                                           var usesemifinish = win.down('textfield[name="use_semi_finished"]').getValue();
                                                           if(isNaN(usefinish))
                                                                   usefinish = 0;
                                                           if(isNaN(usesemifinish))
                                                                   usesemifinish = 0;
                                                           
                                                                           var eq = e.up('form').down('textfield[name="product_rate"]').getValue();
                                                                           if(isNaN(eq) || isNaN(v) || v == 0 || eq == 0)
                                                                           {
                                                                                   e.up('form').down('textfield[name="quantity"]').setValue(0);
                                                                           }
                                                                           else
                                                                           {
                                                                                   var pq = Math.ceil((v - usefinish) / eq - usesemifinish);
                                                                   if(pq < 0) pq = 0;
                                                                   e.up('form').down('textfield[name="quantity"]').setValue(pq);
                                                                           }
                                                                   }
                                           }
                                    }, {
                                                        anchor : '98%',
                                                        fieldLabel : '交货日期 *',
                                                        editable : false,
                                                        name : 'c_deadline',
                                                        xtype: 'datefield',
                                                format: 'Y-m-d',
                                                readOnly : true
                                                }, {
                                                        xtype : 'container',
                                                        layout : 'column',
                                                        items : [ {
                                                                xtype : 'textfield',
                                                                fieldLabel : '使用成品个数',
                                                                name : 'use_finished',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '使用半成品数',
                                                                name : 'use_semi_finished',
                                                                readOnly : true
                                                        }
                                                        ]
                                                }, {
                                                        xtype : 'textareafield',
                                                        grow : true,
                                                        name : 'requirement1',
                                                        fieldLabel : '电镀要求',
                                                        readOnly : true
                                                }, {
                                                        xtype : 'textareafield',
                                                        grow : true,
                                                        name : 'requirement2',
                                                        fieldLabel : '特殊要求',
                                                        readOnly : true
                                                }, {
                                                        anchor : '98%',
                                                        fieldLabel : '生产期限',
                                                        editable : false,
                                                        name : 'deadline',
                                                        xtype: 'datefield',
                                                format: 'Y-m-d',
                                                readOnly : true
                                                }, {
                                                        xtype : 'container',
                                                        layout : 'column',
                                                        items:[
                                                               {
                                                                        xtype : 'container',
                                                                        columnWidth : 0.3,
                                                                        items:[
                                                                       {
                                                                           xtype : 'textfield',
                                                                           name : 'product_rate',
                                                                           fieldLabel : '预成率',
                                                                           width : 160,
                                                                           readOnly : true,
                                                                           listeners:{
                                                                                   change: function(e, v)
                                                                                   {
                                                                                           var usefinish = win.down('textfield[name="use_finished"]').getValue();
                                                                                           var usesemifinish = win.down('textfield[name="use_semi_finished"]').getValue();
                                                                                           if(isNaN(usefinish))
                                                                                                   usefinish = 0;
                                                                                           if(isNaN(usesemifinish))
                                                                                                   usesemifinish = 0;
                                                                                           
                                                                                           var eq = e.up('form').down('textfield[name="e_quantity"]').getValue();
                                                                                           if(isNaN(eq) || isNaN(v) || v == 0 || eq == 0)
                                                                                           {
                                                                                                   e.up('form').down('textfield[name="quantity"]').setValue(0);
                                                                                           }
                                                                                           else
                                                                                           {
                                                                                                   //生产数量（自动生成）=（订单数量-使用成品数）/成品率 - 半成品数
                                                                                                   var pq = Math.ceil((eq - usefinish) / v - usesemifinish);
                                                                                                   if(pq < 0) pq = 0;
                                                                                                   e.up('form').down('textfield[name="quantity"]').setValue(pq);
                                                                                           }
                                                                                   }
                                                                           }
                                                                       }
                                                               ]
                                                               },
                                                               {
                                                                        xtype : 'container',
                                                                        columnWidth : 0.4,
                                                                        items:[
                                                                       {
                                                                           xtype : 'textfield',
                                                                           name : 'quantity',
                                                                           readOnly : true,
                                                                           labelWidth : 120,
                                                                           fieldLabel : '生产数量(自动生成)'
                                                                       }
                                                               ]
                                                               }
                                                       ]
                                                }, Ext.create('Ext.form.ComboBox', {
                                                        fieldLabel : '优先级',
                                                        editable : false,
                                                        readOnly: true,
                                                        store : new Ext.data.ArrayStore({
                                                                autoDestory : true,
                                                                fields : [ 'name', 'value' ],
                                                                data : [ [ '紧急','1' ], [ '普通' , '0'] ]
                                                        }),
                                                        queryMode : 'local',
                                                        displayField : 'name',
                                                        valueField : 'value',
                                                        value : '0',
                                                        name : 'priority'
                                                }),
                                                {
                                                        xtype : 'textareafield',
                                                        grow : true,
                                                        name : 'requirement3',
                                                        fieldLabel : '其他要求',
                                                        hidden: true,
                                                        readOnly : true
                                                }, {
                                                        xtype : 'textareafield',
                                                        grow : true,
                                                        name : 'requirement4',
                                                        fieldLabel : '备注',
                                                        readOnly : true
                                                }
                                ]
                                },    
                                {
                                        xtype : 'fieldset',
                                        title : '产品资料',
                                        items :[
                                                {
                                                        id : 'order-image',
                                                        html: '产品图片<br/><img src="" height=400 />'
                                                },{
                                                        id : 'order-drawing',
                                                        html: '产品图纸<br/><img src="" height=400 />'
                                                },{
                                                        xtype : 'textfield',
                                                        fieldLabel : '模具率',
                                                        name : 'mold_rate',
                                                        readOnly : true
                                                },{
                                                        xtype : 'textfield',
                                                        fieldLabel : '机加位',
                                                        name : 'machining_pos',
                                                        readOnly : true
                                                },{
                                                        xtype : 'textfield',
                                                        fieldLabel : '手工位',
                                                        name : 'handwork_pos',
                                                        readOnly : true
                                                },{
                                                        xtype : 'textfield',
                                                        fieldLabel : '抛光难度',
                                                        name : 'polishing',
                                                        readOnly : true
                                                },{
                                                        xtype : 'textfield',
                                                        fieldLabel : '模具号码',
                                                        name : 'mold_code',
                                                        readOnly : true
                                                },{
                                                        xtype : 'textfield',
                                                        fieldLabel : '模具名称',
                                                        name : 'mold_name',
                                                        readOnly : true
                                                },{
                                                        xtype : 'textfield',
                                                        fieldLabel : '模具架',
                                                        name : 'mold_stand_no',
                                                        readOnly : true
                                                }
                                ]
                                },
                                {
                                        xtype : 'fieldset',
                                        title : '部门分配',
                                        items : [
                                                Ext.create('Ext.grid.Panel', {
                                                        id : 'job-grid',
                                                        store : Ext.data.StoreManager.lookup('jobStore'),
                                                        columns : [ {
                                                                header : 'Id',
                                                                dataIndex : 'id',
                                                                hidden : true
                                                        }, {
                                                                header : '所在部门',
                                                                dataIndex : 'section'
                                                        }, {
                                                                header : '生产总数',
                                                                dataIndex : 'total'
                                                        }, {
                                                                header : '未完成数',
                                                                dataIndex : 'remaining'
                                                        }, {
                                                                header : '返工总数',
                                                                dataIndex : 'total_rejected'
                                                        }, {
                                                                header : '完成总数',
                                                                dataIndex : 'finished'
                                                        }, {
                                                                header : '工作状态',
                                                                dataIndex : 'status'
                                                        }, {
                                                                header : '负责人',
                                                                dataIndex : 'assigned_to'
                                                        } ,{
                                                                header : '完成日期',
                                                                dataIndex : 'complete_date'
                                                        } ],
                                                        height : 400,
                                                        renderTo : Ext.getBody(),
                                                        tbar : [ {
                                                                text : '补数',
                                                                type : 'button',
                                                                disabled : location.href.indexOf("order.jsp") >= 0,
                                                                handler : function()
                                                                {
                                                                        var jobWin = Ext.create('Ext.window.Window', {
                                                                                title : '补数详细' ,
                                                                                height : 150,
                                                                                width : 320,
                                                                                layout : 'fit',
                                                                                modal: true,
                                                                                items : [ Ext.create('Ext.form.Panel', {
                                                                                        layout : 'anchor',
                                                                                        border : false,
                                                                                        containScroll : true,
                                                                                        autoScroll : true,
                                                                                        defaults : {
                                                                                                anchor : '98%'
                                                                                        },
                                                                                        items : [ 
                                                                            {
                                                                                xtype : 'hiddenfield',
                                                                                name : 'order_id',
                                                                                value : selected[0].data.id
                                                                            }, {
                                                                                                xtype : 'textfield',
                                                                                                anchor : '98%',
                                                                                                fieldLabel : '补做数量 *',
                                                                                                name : 'quantity'
                                                                                        }, Ext.create('Ext.form.ComboBox', {
                                                                                            fieldLabel: '送交部门 *',
                                                                                            editable: false,
                                                                                            store: Ext.data.StoreManager.lookup('jobTypeStore'),
                                                                                            queryMode: 'local',
                                                                                            displayField: 'name',
                                                                                            valueField: 'name',
                                                                                            name : 'job_type',
                                                                                            renderTo: Ext.getBody()
                                                                                        }),Ext.create('Ext.form.ComboBox', {
                                                                                            fieldLabel: '送交员工',
                                                                                            editable: false,
                                                                                            store: Ext.data.StoreManager.lookup('allUserACStore'),
                                                                                            queryMode: 'local',
                                                                                            displayField: 'name',
                                                                                            valueField: 'id',
                                                                                            name : 'assigned_to',
                                                                                            renderTo: Ext.getBody()
                                                                                        })],
                                                                                        buttons : [ {
                                                                                                text : '确定',
                                                                                                handler : function() {
                                                                                                        var form = this.up('form').getForm();
                                                                                                        
                                                                                                        if(     this.up('form').down('combobox[name="job_type"]').getValue() == null ||
                                                                                                                Ext.String.trim(this.up('form').down('textfield[name="quantity"]').getValue()) == '')
                                                                                                        {
                                                                                                                Ext.Msg.alert('补数结果','带*号资料必须输入');
                                                                                                                return;
                                                                                                        }
                                                                                                        
                                                                                                        form.submit({
                                                                                                                url : 'OrderController?action=addJobToOrder',
                                                                                                                success : function(form, resp) {
                                                                                                                        Ext.Msg.alert('补数结果', resp.result.msg);
                                                                                                                        Ext.data.StoreManager.lookup('jobStore').load();
                                                                                                                        jobWin.close();
                                                                                                                        GetJsonData("OrderController?action=getOrderById",{id: selected[0].data.id}, fillOrderForm);
                                                                                                                },
                                                                                                                failure : function(form, resp) {
                                                                                                                        Ext.Msg.alert('补数结果', resp.result.msg);
                                                                                                                        Ext.data.StoreManager.lookup('jobStore').load();
                                                                                                                        GetJsonData("OrderController?action=getOrderById",{id: selected[0].data.id}, fillOrderForm);
                                                                                                                }
                                                                                                        });
                                                                                                }
                                                                                        }, {
                                                                                                text : '取消',
                                                                                                handler : function() {
                                                                                                        jobWin.close();
                                                                                                }
                                                                                        } ]
                                                                                }) ]
                                                                        });
                                                                        jobWin.show();
                                                                        
                                                                }
                                                        },
                                                        {
                                                                text : '减数',
                                                                type : 'button',
                                                                disabled : location.href.indexOf("order.jsp") >= 0,
                                                                handler : function()
                                                                {
                                                                        var selectedJob;
                                                                        selectedJob = Ext.getCmp('job-grid').getSelectionModel().getSelection();
                                                                        if(selectedJob.length == 0)
                                                                        {
                                                                                Ext.Msg.alert("减数结果", "请选择要减数的工作");
                                                                                return;
                                                                        }
                                                                        var jobWin = Ext.create('Ext.window.Window', {
                                                                                title : '减数详细' ,
                                                                                height : 150,
                                                                                width : 320,
                                                                                layout : 'fit',
                                                                                modal: true,
                                                                                items : [ Ext.create('Ext.form.Panel', {
                                                                                        layout : 'anchor',
                                                                                        border : false,
                                                                                        containScroll : true,
                                                                                        autoScroll : true,
                                                                                        defaults : {
                                                                                                anchor : '98%'
                                                                                        },
                                                                                        items : [ 
                                                                            {
                                                                                xtype : 'hiddenfield',
                                                                                name : 'id',
                                                                                value : selectedJob[0].data.id
                                                                            }, {
                                                                                                xtype : 'textfield',
                                                                                                anchor : '98%',
                                                                                                fieldLabel : '减去数量 *',
                                                                                                name : 'delete_count'
                                                                                        }],
                                                                                        buttons : [ {
                                                                                                text : '确定',
                                                                                                handler : function() {
                                                                                                        var form = this.up('form').getForm();
                                                                                                        form.submit({
                                                                                                                url : 'JobController?action=deleteJobFromOrder',
                                                                                                                success : function(form, resp) {
                                                                                                                        Ext.Msg.alert('减数结果', resp.result.msg);
                                                                                                                        Ext.data.StoreManager.lookup('jobStore').load();
                                                                                                                        jobWin.close();
                                                                                                                        GetJsonData("OrderController?action=getOrderById",{id: selected[0].data.id}, fillOrderForm);
                                                                                                                },
                                                                                                                failure : function(form, resp) {
                                                                                                                        Ext.Msg.alert('减数结果', resp.result.msg);
                                                                                                                        Ext.data.StoreManager.lookup('jobStore').load();
                                                                                                                        GetJsonData("OrderController?action=getOrderById",{id: selected[0].data.id}, fillOrderForm);
                                                                                                                }
                                                                                                        });
                                                                                                }
                                                                                        }, {
                                                                                                text : '取消',
                                                                                                handler : function() {
                                                                                                        jobWin.close();
                                                                                                }
                                                                                        } ]
                                                                                }) ]
                                                                        });
                                                                        jobWin.show();
                                                                }
                                                        }]
                                                }) 
                                        ]
                                }

                                ],
                                buttons : [ {
                                        text : '修改',
                                        disabled : Ext.getCmp('order-tab').getActiveTab().title != "在线订单",
                                        handler : function(){
                                                win.down('button[text="提交"]').enable();
                                                win.down('button[text="取消"]').show();
                                                win.down('button[text="修改"]').hide();
                                                win.down('textfield[name="creator"]').setReadOnly(false);
                                                win.down('textfield[name="customer_code"]').setReadOnly(false);
                                                win.down('textfield[name="customer_name"]').setReadOnly(false);
                                                win.down('textfield[name="product_name"]').setReadOnly(false);                                          
                                                win.down('textfield[name="use_finished"]').setReadOnly(false);
                                                win.down('textfield[name="use_semi_finished"]').setReadOnly(false);
                                                win.down('textfield[name="deadline"]').setReadOnly(false);
                                                win.down('textfield[name="c_deadline"]').setReadOnly(false);
                                                win.down('textfield[name="requirement1"]').setReadOnly(false);
                                                win.down('textfield[name="requirement2"]').setReadOnly(false);
                                                win.down('textfield[name="requirement4"]').setReadOnly(false);
                                                win.down('textfield[name="product_rate"]').setReadOnly(false);
                                                win.down('textfield[name="e_quantity"]').setReadOnly(false);
                                                win.down('combobox[name="priority"]').setReadOnly(false);
                                        }
                                }, {
                                        text : '提交',
                                        disabled : true,
                                        handler : function() {
                                                
                                                var form = this.up('form').getForm();
                                                if(this.up('form').down('combobox[name="creator"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="customer_name"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="customer_code"]').getValue() == null ||
                                                                this.up('form').down('combobox[name="product_name"]').getValue() == null ||
                                                                this.up('form').down('datefield[name="deadline"]').getValue() == null ||
                                                                this.up('form').down('datefield[name="c_deadline"]').getValue() == null
                                                        )
                                                {
                                                        Ext.Msg.alert("修改结果","带*号资料必须输入");
                                                        return;
                                                }
                                                
                                                win.down('textfield[name="ordernumber"]').enable();
                                                win.down('textfield[name="deadline"]').enable();
                                                win.down('textfield[name="requirement4"]').enable();
                                                win.down('textfield[name="product_rate"]').enable();
                                                win.down('textfield[name="e_quantity"]').enable();
                                                win.down('textfield[name="quantity"]').enable();
                                                win.down('combobox[name="priority"]').enable();
                                                win.down('combobox[name="priority"]').enable();
                                                win.down('textfield[name="ordernumber"]').enable();
                                                win.down('textfield[name="creator"]').enable();
                                                win.down('textfield[name="customer_code"]').enable();
                                                win.down('textfield[name="customer_name"]').enable();
                                                win.down('textfield[name="product_name"]').enable();
                                                win.down('textfield[name="product_our_name"]').enable();
                                                win.down('textfield[name="use_finished"]').enable();
                                                win.down('textfield[name="use_semi_finished"]').enable();
                                                win.down('textfield[name="c_deadline"]').enable();
                                                win.down('textfield[name="requirement1"]').enable();
                                                win.down('textfield[name="requirement2"]').enable();

                                                form.submit({
                                                        url : 'OrderController?action=updateOrder',
                                                        success : function(form, action) {
                                                                Ext.Msg.alert('修改結果', action.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                win.close();
                                                        },
                                                        failure : function(form, action) {
                                                                Ext.Msg.alert('修改結果', action.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                
                                                                win.down('textfield[name="ordernumber"]').disable();
                                                                win.down('textfield[name="deadline"]').disable();
                                                                win.down('textfield[name="requirement4"]').disable();
                                                                win.down('textfield[name="product_rate"]').disable();
                                                                win.down('textfield[name="e_quantity"]').disable();
                                                                win.down('textfield[name="quantity"]').disable();
                                                                win.down('combobox[name="priority"]').disable();
                                                                win.down('combobox[name="priority"]').disable();
                                                                win.down('textfield[name="ordernumber"]').disable();
                                                                win.down('textfield[name="creator"]').disable();
                                                                win.down('textfield[name="customer_code"]').disable();
                                                                win.down('textfield[name="customer_name"]').disable();
                                                                win.down('textfield[name="product_name"]').disable();
                                                                win.down('textfield[name="product_our_name"]').disable();
                                                                win.down('textfield[name="use_finished"]').disable();
                                                                win.down('textfield[name="use_semi_finished"]').disable();
                                                                win.down('textfield[name="c_deadline"]').disable();
                                                                win.down('textfield[name="requirement1"]').disable();
                                                                win.down('textfield[name="requirement2"]').disable();
                                                        }
                                                });
                                                
                                        }
                                }, {
                                        text : '取消',
                                        hidden : true,
                                        handler : function()
                                        {
                                                win.down('button[text="提交"]').disable();
                                                win.down('button[text="修改"]').show();
                                                win.down('button[text="取消"]').hide();
                                                win.down('textfield[name="creator"]').setReadOnly(true);
                                                win.down('textfield[name="customer_code"]').setReadOnly(true);
                                                win.down('textfield[name="customer_name"]').setReadOnly(true);
                                                win.down('textfield[name="product_name"]').setReadOnly(true);
                                                win.down('textfield[name="use_finished"]').setReadOnly(true);
                                                win.down('textfield[name="use_semi_finished"]').setReadOnly(true);
                                                win.down('textfield[name="deadline"]').setReadOnly(true);
                                                win.down('textfield[name="c_deadline"]').setReadOnly(true);
                                                win.down('textfield[name="requirement1"]').setReadOnly(true);
                                                win.down('textfield[name="requirement2"]').setReadOnly(true);
                                                win.down('textfield[name="requirement4"]').setReadOnly(true);
                                                win.down('textfield[name="product_rate"]').setReadOnly(true);
                                                win.down('textfield[name="e_quantity"]').setReadOnly(true);
                                                win.down('combobox[name="priority"]').setReadOnly(true);
                                                GetJsonData("OrderController?action=getOrderById",{id: selected[0].data.id}, fillOrderForm);
                                        }
                                } ]
                        }) ]
                });
        
        function fillOrderForm(data)
        {
                if(data.data.length > 0)
                        {
                                win.down('hiddenfield[name="orderid"]').setValue(data.data[0].id);
                                win.down('textfield[name="ordernumber"]').setValue(data.data[0].number);
                                win.down('textfield[name="creator"]').setRawValue(data.data[0].creator);
                                win.down('textfield[name="customer_code"]').setRawValue(data.data[0].customer_code);
                                win.down('textfield[name="customer_name"]').setRawValue(data.data[0].customer_name);
                                win.down('textfield[name="product_name"]').setRawValue(data.data[0].product_name);
                                win.down('textfield[name="product_our_name"]').setRawValue(data.data[0].product_our_name);
                                win.down('textfield[name="use_finished"]').setValue(data.data[0].use_finished);
                                win.down('textfield[name="use_semi_finished"]').setValue(data.data[0].use_semi_finished);
                                win.down('textfield[name="deadline"]').setRawValue(data.data[0].deadline);
                                win.down('textfield[name="c_deadline"]').setRawValue(data.data[0].c_deadline);
                                win.down('textfield[name="requirement1"]').setValue(data.data[0].requirement_1);
                                win.down('textfield[name="requirement2"]').setValue(data.data[0].requirement_2);
                                win.down('textfield[name="requirement4"]').setValue(data.data[0].requirement_4);
                                win.down('textfield[name="product_rate"]').setValue(data.data[0].product_rate);
                                win.down('textfield[name="e_quantity"]').setValue(data.data[0].e_quantity);
                                win.down('combobox[name="priority"]').setValue(data.data[0].priority);
                                win.down('combobox[name="priority"]').setRawValue(data.data[0].priority == 1 ? "紧急":"普通");
                        }
                else
                        {
                        Ext.Msg.alert("查看订单", "获取订单详细资料失败");
                        return;
                        }
        }
                
                GetJsonData("OrderController?action=getOrderById",{id: selected[0].data.id}, fillOrderForm);
                
                function fillProductForm(data)
                {
                        if(!data)
                        {
                                Ext.Msg.alert("查看订单", "获取产品详细资料失败");
                                win.close();
                                Ext.data.StoreManager.lookup('orderStore').load();
                                return;
                        }
                        else
                        {
                                Ext.getCmp('order-image').update('产品图片(点击放大)<br/><a target="_blank" href="ProductController?action=getProductImage&name=' + 
                                                selected[0].data.product_name + '"><img src="ProductController?action=getProductImage&name=' + 
                                                selected[0].data.product_name + '" height=400 /></a>');
                                Ext.getCmp('order-drawing').update('产品图纸(点击放大)<br/><a target="_blank" href="ProductController?action=getProductDrawing&name=' + 
                                                selected[0].data.product_name + '"><img src="ProductController?action=getProductDrawing&name=' + 
                                                selected[0].data.product_name + '" height=400 />');
                                win.down('textfield[name="mold_rate"]').setValue(data.mold_rate);
                                win.down('textfield[name="machining_pos"]').setValue(data.machining_pos);
                                win.down('textfield[name="handwork_pos"]').setValue(data.handwork_pos);
                                win.down('textfield[name="polishing"]').setValue(data.polishing);
                                win.down('textfield[name="mold_code"]').setValue(data.mold_code);
                                win.down('textfield[name="mold_name"]').setValue(data.mold_name);
                                win.down('textfield[name="mold_stand_no"]').setValue(data.mold_stand_no);
                        }
                }       
                GetJsonData("ProductController?action=getProductByName",{name: selected[0].data.product_name}, fillProductForm);
                Ext.data.StoreManager.lookup('jobStore').proxy.url = 'JobController?action=getJobByOrder&orderid=' + selected[0].data.id;
                Ext.data.StoreManager.lookup('jobStore').load();
                
                if(location.href.indexOf("order.jsp") >= 0)
                {
                        win.down('textfield[name="ordernumber"]').disable();
                        win.down('textfield[name="deadline"]').disable();
                        win.down('textfield[name="requirement4"]').disable();
                        win.down('textfield[name="product_rate"]').disable();
                        win.down('textfield[name="e_quantity"]').disable();
                        win.down('textfield[name="quantity"]').disable();
                        win.down('combobox[name="priority"]').disable();
                        win.down('combobox[name="priority"]').disable();
                }
                else
                {
                        win.down('textfield[name="ordernumber"]').disable();
                        win.down('textfield[name="creator"]').disable();
                        win.down('textfield[name="customer_code"]').disable();
                        win.down('textfield[name="customer_name"]').disable();
                        win.down('textfield[name="product_name"]').disable();
                        win.down('textfield[name="product_our_name"]').disable();
                        win.down('textfield[name="use_finished"]').disable();
                        win.down('textfield[name="use_semi_finished"]').disable();
                        win.down('textfield[name="c_deadline"]').disable();
                        win.down('textfield[name="requirement1"]').disable();
                        win.down('textfield[name="requirement2"]').disable();
                }
                win.show();     
    },
    
    onDeleteOrderClicked: function(){
        var win = Ext.create('Ext.window.Window', {
                        title : '删除订单',
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
                                         html: '<table><tr height=10><td></td></tr><tr><td>是否删除该订单?</td></tr></table>'
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
                                
                                 var selected;
                                                if(Ext.getCmp('order-tab').getActiveTab().title == "在线订单")
                                                {
                                                        selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                                                }
                                                else
                                                {
                                                        selected = Ext.getCmp('completed-order-grid').getSelectionModel().getSelection();
                                                }
                                
                                         if(selected.length == 0)
                                         {
                                                 Ext.Msg.alert("删除订单","请选择要删除的订单");
                                                 win.close();
                                                 return;
                                         }
                                         
                                         this.up('form').down('hiddenfield[name="id"]').setValue(selected[0].data.id);
                                         
                                         this.up('form').getForm().submit({
                                                url : 'OrderController?action=deleteOrder',
                                                        success : function(form, resp) {
                                                                Ext.Msg.alert('删除结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                Ext.data.StoreManager.lookup('completedOrderStore').load();
                                                                win.close();
                                                        },
                                                        failure : function(form, resp) {
                                                                Ext.Msg.alert('删除结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                Ext.data.StoreManager.lookup('completedOrderStore').load();
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
                
        var selected;
        if(Ext.getCmp('order-tab').getActiveTab().title == "在线订单")
                {
                selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                }
        else
                {
                selected = Ext.getCmp('completed-order-grid').getSelectionModel().getSelection();
                }
                
                if(selected.length == 0)
                {
                        Ext.Msg.alert("删除订单","请选择要删除的订单");
                        return;
                }
                else
                {
                        win.show();     
                }
    },
    onPauseOrderClicked : function()
    {
        var win = Ext.create('Ext.window.Window', {
                        title : '暂停订单',
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
                                         html: '<table><tr height=10><td></td></tr><tr><td>是否暂停该订单?</td></tr></table>'
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
                                 
                                 var selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                                 if(selected.length == 0)
                                         {
                                         Ext.Msg.alert("暂停订单","请选择要暂停的订单");
                                         win.close();
                                         return;
                                         }
                                 
                                 this.up('form').down('hiddenfield[name="id"]').setValue(selected[0].data.id);
                                 
                                 this.up('form').getForm().submit({
                                        url : 'OrderController?action=pauseOrder',
                                                        success : function(form, resp) {
                                                                Ext.Msg.alert('暂停结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                win.close();
                                                        },
                                                        failure : function(form, resp) {
                                                                Ext.Msg.alert('暂停结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
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
                
                var selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                if(selected.length == 0)
                {
                        Ext.Msg.alert("暂停订单","请选择要暂停的订单");
                        return;
                }
                else
                {
                        win.show();     
                }
    },
    onCancelOrderClicked : function()
    {
        var win = Ext.create('Ext.window.Window', {
                        title : '取消订单',
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
                                         html: '<table><tr height=10><td></td></tr><tr><td>是否取消该订单?</td></tr></table>'
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
                                 
                                 var selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                                 if(selected.length == 0)
                                         {
                                         Ext.Msg.alert("取消订单","请选择要取消的订单");
                                         win.close();
                                         return;
                                         }
                                 
                                 this.up('form').down('hiddenfield[name="id"]').setValue(selected[0].data.id);
                                 
                                 this.up('form').getForm().submit({
                                        url : 'OrderController?action=cancelOrder',
                                                        success : function(form, resp) {
                                                                Ext.Msg.alert('取消结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                win.close();
                                                        },
                                                        failure : function(form, resp) {
                                                                Ext.Msg.alert('取消结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
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
                
                var selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                if(selected.length == 0)
                {
                        Ext.Msg.alert("取消订单","请选择要取消的订单");
                        return;
                }
                else
                {
                        win.show();     
                }
    },
    onResumeOrderClicked : function()
    {
        var win = Ext.create('Ext.window.Window', {
                        title : '恢复订单',
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
                                         html: '<table><tr height=10><td></td></tr><tr><td>是否恢复该订单?</td></tr></table>'
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
                                 
                                 var selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                                 if(selected.length == 0)
                                         {
                                         Ext.Msg.alert("恢复订单","请选择要恢复的订单");
                                         win.close();
                                         return;
                                         }
                                 
                                 this.up('form').down('hiddenfield[name="id"]').setValue(selected[0].data.id);
                                 
                                 this.up('form').getForm().submit({
                                        url : 'OrderController?action=resumeOrder',
                                                        success : function(form, resp) {
                                                                Ext.Msg.alert('恢复结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
                                                                win.close();
                                                        },
                                                        failure : function(form, resp) {
                                                                Ext.Msg.alert('恢复结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('orderStore').load();
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
                
                var selected = Ext.getCmp('order-grid').getSelectionModel().getSelection();
                if(selected.length == 0)
                {
                        Ext.Msg.alert("恢复订单","请选择要恢复的订单");
                        return;
                }
                else
                {
                        win.show();     
                }
    },
    onSummaryTotalClicked : function()
    {
        Ext.data.StoreManager.lookup('orderStore').on('load', function countTotal() {
                var total = 0;
                Ext.data.StoreManager.lookup('orderStore').each(
                                function(item, index, totalItems ) {
                                        if(item.data ['status'] == "进行中")
                                                total += item.data ['quantity'];
                                }
                        );
                Ext.data.StoreManager.lookup('orderStore').removeListener('load', countTotal);
                Ext.Msg.alert("统计","生产总数为 : " + total);
        });
        
        Ext.data.StoreManager.lookup('orderStore').load();
    },
    onSummaryOrderTotalClicked : function()
    {
        Ext.data.StoreManager.lookup('orderStore').on('load', function countTotal() {
                var total = 0;
                Ext.data.StoreManager.lookup('orderStore').each(
                                function(item, index, totalItems ) {
                                        if(item.data ['status'] == "进行中")
                                                total += item.data ['e_quantity'];
                                }
                        );
                Ext.data.StoreManager.lookup('orderStore').removeListener('load', countTotal);
                Ext.Msg.alert("统计","订单总数为 : " + total);
        });
        
        Ext.data.StoreManager.lookup('orderStore').load();
    }
});