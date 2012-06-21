Ext.define('productionlog.view.v_productionlog', {
        extend : 'Ext.form.Panel',

        alias : 'widget.productionlogform',

        title : '',

        id : 'productionlogform',

        initComponent : function() {
                this.layout = 'anchor';
                
                
                Ext.create('Ext.data.Store', {
                        storeId : 'productionLogStore',
                        model : 'ProductionLogData',
                        proxy : {
                                type : 'ajax',
                                url : 'ReportController?action=generateProductLogReportByDateAndSection',
                                reader : {
                                        type : 'json',
                                        root : 'data',
                                        model : 'ProductionLogData'
                                }
                        }
                });
                
                
                this.items = [ Ext.create('Ext.tab.Panel', {
                        anchor : '100%',
                        items : [{
                                title : '生产记录',
                                height : GET_HEIGHT(), 
                                items : [ Ext.create('Ext.grid.Panel', { 
                                id : 'productlog-grid',
                                store: Ext.data.StoreManager.lookup('productionLogStore'),
                                columns : [{
                                                header : '料号',
                                                dataIndex : 'product_our_name'
                                        }, {
                                                header : '完成总数',
                                                dataIndex : 'finished'
                                        }, {
                                                header : '废品数',
                                                dataIndex : 'disuse'
                                        }, {
                                                header : '返工数',
                                                dataIndex : 'rejected'
                                        }, {
                                                header : '订单',
                                                dataIndex : 'orders',
                                                width : 400
                                        }, {
                                                header : '生产期限',
                                                dataIndex : 'deadlines',
                                                width : 400
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
                                        }],
                                        tbar : [
                                            {
                                                labelAlign : 'right',
                                                        fieldLabel : '起始生产日期',
                                                        editable : false,
                                                        name : 'date',
                                                        id : 'productlog_date',
                                                        xtype: 'datefield',
                                                maxValue: new Date(),
                                                value: new Date(),
                                                format: 'Y-m-d'
                                            },
                                            {
                                                labelAlign : 'right',
                                                        fieldLabel : '到生产日期',
                                                        editable : false,
                                                        name : 'end_date',
                                                        id : 'productlog_end_date',
                                                        xtype: 'datefield',
                                                maxValue: new Date(),
                                                value: new Date(),
                                                format: 'Y-m-d'
                                            },
                                            Ext.create('Ext.form.ComboBox', {
                                                labelAlign : 'right',
                                                    fieldLabel: '部门',
                                                    id : 'productlog_section',
                                                    editable: false,
                                                    store: Ext.data.StoreManager.lookup('jobTypeStore'),
                                                    queryMode: 'local',
                                                    displayField: 'name',
                                                    valueField: 'name',
                                                    name : 'job_type'
                                                }),
                                                {
                                                xtype : 'button',
                                                text : '刷新记录'
                                                },
                                                {
                                                xtype : 'button',
                                                text : '汇出记录'
                                                }
                            ]
                        })]
                        }]
                }) ];

                this.callParent(arguments);
        }
});