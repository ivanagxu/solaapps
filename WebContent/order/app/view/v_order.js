Ext.define('order.view.v_order', {
        extend : 'Ext.form.Panel',

        alias : 'widget.order_table',

        id : 'order_table',

        initComponent : function() {
                

                this.layout = 'anchor';

                Ext.create('Ext.data.Store', {
                        storeId : 'orderStore',
                        model : 'OrderData',
                        proxy : {
                                type : 'ajax',
                                url : 'OrderController?action=getMyOrderList',
                                reader : {
                                        type : 'json',
                                        root : 'data',
                                        model : 'OrderData'
                                }
                        },
                        sortOnLoad: true ,
                        sorters: { property: 'priority', direction : 'DESC' }
                });
                
                Ext.create('Ext.data.Store', {
                        storeId : 'completedOrderStore',
                        model : 'OrderData',
                        proxy : {
                                type : 'ajax',
                                url : 'OrderController?action=getCompletedOrderList',
                                reader : {
                                        type : 'json',
                                        root : 'data',
                                        model : 'OrderData'
                                }
                        },
                        sortOnLoad: true ,
                        sorters: { property: 'priority', direction : 'DESC' }
                });
                
                Ext.create('Ext.data.Store', {
                        storeId : 'jobStore',
                        model : 'JobData',
                        proxy : {
                                type : 'ajax',
                                url : 'JobController?action=getJobByOrder',
                                reader : {
                                        type : 'json',
                                        root : 'data',
                                        model : 'JobData'
                                }
                        }
                });
                
                Ext.create('Ext.data.Store', {
                        storeId : 'productRateStore',
                        model : 'ProductRate',
                        proxy : {
                                type : 'ajax',
                                url : 'ReportController?action=generateProductRateReportByProduct',
                                reader : {
                                        type : 'json',
                                        root : 'data',
                                        model : 'ProductRate'
                                }
                        }
                });
                var sm = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
                var sm2 = Ext.create('Ext.selection.CheckboxModel',{mode : 'SINGLE'});
                this.items = [
                        Ext.create('Ext.tab.Panel', {
                                anchor : '100%',
                                id : 'order-tab',
                                items : [ {
                                        title : '在线订单',
                                        items : [
                                                Ext.create('Ext.grid.Panel', {
                                                        id : 'order-grid',
                                                        height : GET_HEIGHT(),  
                                                        store : Ext.data.StoreManager.lookup('orderStore'),
                                                        selModel : sm,
                                                        columns : [
                                                    {
                                                                header : 'Id',
                                                                dataIndex : 'id',
                                                                hidden : true
                                                        }, {
                                                                header : '优先级',
                                                                dataIndex : 'priority',
                                                                renderer : function(val)
                                                                {
                                                                        if(val == 1)
                                                                                return '紧急';
                                                                        else
                                                                                return '普通';
                                                                },
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '订单号',
                                                                dataIndex : 'number',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '跟单人员',
                                                                dataIndex : 'creator',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '客户名称',
                                                                dataIndex : location.href.indexOf("production.jsp") >= 0 ? '' : 'customer_name',
                                                                hidden : location.href.indexOf("production.jsp") >= 0,
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '客户代码',
                                                                dataIndex : 'customer_code',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '客户料号',
                                                                dataIndex : location.href.indexOf("production.jsp") >= 0 ? '' : 'product_name',
                                                                hidden : location.href.indexOf("production.jsp") >= 0,
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '料号',
                                                                dataIndex : 'product_our_name',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '电镀要求',
                                                                dataIndex : 'requirement_1',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '特殊要求',
                                                                dataIndex : 'requirement_2',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '订单数量',
                                                                dataIndex : 'e_quantity',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '生产数量',
                                                                dataIndex : 'quantity',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '生产期限',
                                                                dataIndex : 'deadline',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '交货日期',
                                                                dataIndex : 'c_deadline',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '产品图片',
                                                                dataIndex : 'image',
                                                                renderer: function(val)
                                                                {
                                                                        if(val == '')
                                                                                return '';
                                                                        else
                                                                                return '<a target="_blank" onclick=DISPLAY_IMAGE_WINDOW("ProductController?action=getProductImage&name=' + val + '",' + '"ProductController?action=getProductDrawing&name=' + val + '"' + ')><img src="resources/images/picture.png"/></a>';
                                                                }
                                                        }, {
                                                                header : '产品图纸',
                                                                dataIndex : 'drawing',
                                                                hidden : true,
                                                                renderer: function(val)
                                                                {
                                                                        if(val == '')
                                                                                return '';
                                                                        else
                                                                                return '<a target="_blank" onclick=DISPLAY_IMAGE_WINDOW("ProductController?action=getProductImage&name=' + val + '",' + '"ProductController?action=getProductDrawing&name=' + val + '"' + ')><img src="resources/images/picture.png"/></a>';
                                                                }
                                                        }, {
                                                                header : '订单状态',
                                                                dataIndex : 'status',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '备注',
                                                                dataIndex : 'requirement_4',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }],
                                                        renderTo : Ext.getBody(),
                                                        tbar : [ {
                                                                xtype : 'button',
                                                                text : '创建订单'
                                                        }, {
                                                                xtype : 'button',
                                                                text : '查看订单'
                                                        }, {
                                                                xtype : 'button',
                                                                text : '审核订单'
                                                        }, {
                                                                xtype : 'button',
                                                                text : '暂停订单'
                                                        }, {
                                                                xtype : 'button',
                                                                text : '恢复订单'
                                                        }, {
                                                                xtype : 'button',
                                                                text : '取消订单'
                                                        }, {
                                                                xtype : 'button',
                                                                text : '删除订单'
                                                        }, {
                                                                xtype : 'button',
                                                                text : '统计',
                                                                menu: {
                                                                        showSeparator: true,
                                                                        items: [{
                                                                                text: '订单总数',
                                                                                iconCls: 'task-icon'
                                                                        },{
                                                                                text: '生产总数',
                                                                                iconCls: 'task-icon'
                                                                        }]
                                                                }
                                                        }],
                                                        viewConfig:{
                                                                getRowClass : function(rec, index)
                                                                {
                                                                        if(rec.data.status == "审核中")
                                                                        {
                                                                                return 'green-row';
                                                                        }
                                                                        else
                                                                        {
                                                                                return '';
                                                                        }
                                                                }
                                                        },
                                                        features: [{
                                                ftype: 'filters',
                                                encode: true,
                                                local: true
                                            }]
                                                })
                                ]
                                }, {
                                        title : '完成订单',
                                        items: [
                                                Ext.create('Ext.grid.Panel', {
                                                        id : 'completed-order-grid',
                                                        height : GET_HEIGHT(), 
                                                        store : Ext.data.StoreManager.lookup('completedOrderStore'),
                                                        selModel : sm2,
                                                        columns : [
                                                    {
                                                                header : 'Id',
                                                                dataIndex : 'id',
                                                                hidden : true
                                                        }, {
                                                                header : '优先级',
                                                                dataIndex : 'priority',
                                                                renderer : function(val)
                                                                {
                                                                        if(val == 1)
                                                                                return '紧急';
                                                                        else
                                                                                return '普通';
                                                                },
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '订单号',
                                                                dataIndex : 'number',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '跟单人员',
                                                                dataIndex : 'creator',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '客户名称',
                                                                dataIndex : location.href.indexOf("production.jsp") >= 0 ? '' : 'customer_name',
                                                                hidden : location.href.indexOf("production.jsp") >= 0,
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '客户代码',
                                                                dataIndex : 'customer_code',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '客户料号',
                                                                dataIndex : location.href.indexOf("production.jsp") >= 0 ? '' : 'product_name',
                                                                hidden : location.href.indexOf("production.jsp") >= 0,
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '料号',
                                                                dataIndex : 'product_our_name',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '电镀要求',
                                                                dataIndex : 'requirement_1',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '特殊要求',
                                                                dataIndex : 'requirement_2',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '订单数量',
                                                                dataIndex : 'e_quantity',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '生产数量',
                                                                dataIndex : 'quantity',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '生产期限',
                                                                dataIndex : 'deadline',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '交货日期',
                                                                dataIndex : 'c_deadline',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '产品图片',
                                                                dataIndex : 'image',
                                                                renderer: function(val)
                                                                {
                                                                        if(val == '')
                                                                                return '';
                                                                        else
                                                                                return '<a target="_blank" onclick=DISPLAY_IMAGE_WINDOW("ProductController?action=getProductImage&name=' + val + '",' + '"ProductController?action=getProductDrawing&name=' + val + '"' + ')><img src="resources/images/picture.png"/></a>';
                                                                }
                                                        }, {
                                                                header : '产品图纸',
                                                                dataIndex : 'drawing',
                                                                hidden : true,
                                                                renderer: function(val)
                                                                {
                                                                        if(val == '')
                                                                                return '';
                                                                        else
                                                                                return '<a target="_blank" onclick=DISPLAY_IMAGE_WINDOW("ProductController?action=getProductImage&name=' + val + '",' + '"ProductController?action=getProductDrawing&name=' + val + '"' + ')><img src="resources/images/picture.png"/></a>';
                                                                }
                                                        }, {
                                                                header : '订单状态',
                                                                dataIndex : 'status',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }, {
                                                                header : '备注',
                                                                dataIndex : 'requirement_4',
                                                                filter: {
                                                        type: 'string'
                                                    }
                                                        }],
                                                        renderTo : Ext.getBody(),
                                                        tbar : [{
                                                                xtype : 'button',
                                                                text : '查看订单'
                                                        }, {
                                                                xtype : 'button',
                                                                text : '删除订单'
                                                        } ],
                                                        features: [{
                                                ftype: 'filters',
                                                encode: true,
                                                local: true
                                            }]
                                                })
                                ]
                                }
                                ]
                        }
                        )
                ];

                this.callParent(arguments);
                if(location.href.indexOf("production.jsp") >= 0)
                {
                        this.down('button[text="创建订单"]').hide();
                        this.down('button[text="暂停订单"]').hide();
                        this.down('button[text="取消订单"]').hide();
                        this.down('button[text="恢复订单"]').hide();
                        this.down('button[text="删除订单"]').hide();
                }
                else if(location.href.indexOf("order.jsp") >= 0)
                {
                        this.down('button[text="审核订单"]').hide();
                }
        }
});