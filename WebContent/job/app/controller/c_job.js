Ext.define('job.controller.c_job', {
    extend: 'Ext.app.Controller',

    views: [
            'v_job'
        ],
    
    init: function() {
        this.control({
            'viewport > panel': {
                render: this.onPanelRendered
            },
            'panel[id="job-grid"]' :{
                render: this.onJobGridRendered
            },
            'button[text="移交工作"]' :{
                click: this.onCompleteClick
            },
            'button[text="查看订单"]' :{
                click: this.onViewJobClick
            },
            /////////For mold module
            'panel[id=mold-grid]' : {
                                render : this.onMoldGridRendered
                        },
                        'button[text=添加模具]' : {
                                click : this.onAddMoldClick
                        },
                        'button[text=删除模具]' : {
                                click : this.onDeleteMoldClick
                        },
            'menuitem[text="未完成数"]' : {
                click : this.onSummaryIncompleteTotalClicked
            }
        });
    },

    onPanelRendered: function() {
        //console.log('Job module is loaded');
    },
    
    onJobGridRendered: function() {
        Ext.data.StoreManager.lookup('jobStore').load();
    },
    
    onViewJobClick : function(){
        var selected = Ext.getCmp('job-grid').getSelectionModel().getSelection();
                if(selected.length == 0)
                {
                        Ext.Msg.alert("查看订单","请选择要查看的订单");
                        return;
                }
        
        var win = Ext.create('Ext.window.Window', {
                        title : '订单资料',
                        height : 500,
                        width : 800,
                        layout : 'fit',
                        autoDestory : true,
                        modal: true,
                        items : [
                        Ext.create('Ext.form.Panel', {
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
                                                                xtype : 'textfield',
                                                                fieldLabel : '订单号码',
                                                                name : 'ordernumber',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '跟单人员',
                                                                name : 'creator',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '客户代码',
                                                                name : 'customer_code',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '料号',
                                                                name : 'product_our_name',
                                                                readOnly : true
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
                                                                xtype : 'textfield',
                                                                fieldLabel : '未完成数',
                                                                name : 'remaining',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '生产期限',
                                                                name : 'deadline',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '所在部门',
                                                                name : 'section',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '优先级',
                                                                name : 'priority',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '工作状态',
                                                                name : 'status',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '订单状态',
                                                                name : 'order_status',
                                                                readOnly : true
                                                        }, {
                                                                xtype : 'textfield',
                                                                fieldLabel : '负责人',
                                                                name : 'assigned_to',
                                                                readOnly : true
                                                        }
                                                ]
                                        },
                                        {
                                                xtype : 'fieldset',
                                                title : '产品资料',
                                                defaultType : 'textfield',
                                                layout : 'anchor',
                                                defaults : {
                                                        anchor : '100%'
                                                },
                                                items:[
                                                        {
                                                                id : 'job-image',
                                                                html: '产品图片<br/><img src="" height=400 />'
                                                        },{
                                                                id : 'job-drawing',
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
                                                                store : Ext.data.StoreManager.lookup('orderJobStore'),
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
                                                                renderTo : Ext.getBody()
                                                        }) 
                                                ]
                                        }
                            ]
                        })
                ],
                buttons : [ {
                                text : '移交工作',
                                handler : this.onCompleteClick
                } ]
                }
        );
        
        function fillOrderForm(data)
        {
                if(data.data.length > 0)
                        {
                                win.down('textfield[name="ordernumber"]').setValue(data.data[0].number);
                                win.down('textfield[name="creator"]').setRawValue(data.data[0].creator);
                                win.down('textfield[name="customer_code"]').setRawValue(data.data[0].customer_code);
                                win.down('textfield[name="product_our_name"]').setRawValue(data.data[0].product_our_name);
                                win.down('textfield[name="deadline"]').setRawValue(data.data[0].deadline);
                                win.down('textfield[name="requirement1"]').setValue(data.data[0].requirement_1);
                                win.down('textfield[name="requirement2"]').setValue(data.data[0].requirement_2);
                                win.down('textfield[name="remaining"]').setValue(selected[0].data.remaining);
                                win.down('textfield[name="status"]').setValue(selected[0].data.status);
                                win.down('textfield[name="section"]').setValue(selected[0].data.section);
                                win.down('textfield[name="priority"]').setValue(data.data[0].priority == 1 ? "紧急":"普通");
                                win.down('textfield[name="order_status"]').setValue(data.data[0].status);
                                win.down('textfield[name="assigned_to"]').setValue(selected[0].data.assigned_to);
                        }
                else
                        {
                        Ext.Msg.alert("查看订单", "获取订单详细资料失败");
                        Ext.data.StoreManager.lookup('jobStore').load();
                        return;
                        }
        }
                
                GetJsonData("OrderController?action=getOrderById",{id: selected[0].data.order_id}, fillOrderForm);
                
                function fillProductForm(data)
                {
                        if(!data)
                        {
                                Ext.Msg.alert("查看订单", "获取产品详细资料失败");
                                Ext.data.StoreManager.lookup('jobStore').load();
                                return;
                        }
                        else
                        {
                                Ext.getCmp('job-image').update('产品图片(点击放大)<br/><a target="_blank" href="ProductController?action=getProductImage&name=' + 
                                                selected[0].data.product_name + '"><img src="ProductController?action=getProductImage&name=' + 
                                                selected[0].data.product_name + '" height=400 /></a>');
                                Ext.getCmp('job-drawing').update('产品图纸(点击放大)<br/><a target="_blank" href="ProductController?action=getProductDrawing&name=' + 
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
                Ext.data.StoreManager.lookup('orderJobStore').proxy.url = 'JobController?action=getJobByOrder&orderid=' + selected[0].data.order_id;
                Ext.data.StoreManager.lookup('orderJobStore').load();
        win.show();
    },
    
    onCompleteClick: function(){
        Ext.data.StoreManager.lookup('jobTypeStore').load();
        Ext.data.StoreManager.lookup('allUserACStore').load();
        var win = Ext.create('Ext.window.Window', {
                        title : '完成工作' ,
                        height : 480,
                        width : 320,
                        layout : 'fit',
                        modal: true,
                        items : [ Ext.create('Ext.form.Panel', {
                                layout : 'anchor',
                                border : false,
                                baseCls: 'x-plain',
                                containScroll : true,
                                autoScroll : true,
                                defaults : {
                                        anchor : '98%'
                                },
                                items : [ {
                                        xtype : 'textfield',
                                        anchor : '98%',
                                        fieldLabel : '总数',
                                        name : 'total',
                                        readOnly: true
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
                                }),{
                                        xtype : 'textfield',
                                        anchor : '98%',
                                        fieldLabel : '送交数 *',
                                        name : 'complete_count',
                                        listeners : {
                                                focus : function() {
                                                        //this.up('form').down('textfield[name="disuse_count"]').setValue('');
                                                }
                                        }
                                }, {
                                        xtype : 'textfield',
                                        anchor : '98%',
                                        fieldLabel : '废品数 *',
                                        name : 'disuse_count',
                                        listeners : {
                                                focus : function() {
                                                        //this.up('form').down('textfield[name="complete_count"]').setValue('');
                                                }
                                        }
                                }, {
                                        xtype : 'checkbox',
                                        anchor : '98%',
                                        fieldLabel : '完成',
                                        checked: false,
                                        name : 'is_completed'
                                }, {
                                        xtype : 'checkbox',
                                        anchor : '98%',
                                        fieldLabel : '返工',
                                        checked: false,
                                        name : 'is_rejected'
                                }, {
                                        xtype : 'textareafield',
                                        grow : true,
                                        name : 'finish_remark',
                                        fieldLabel : '废品备注'
                                }, {
                                        xtype: 'hiddenfield',
                                        name: 'id'
                                }],
                                buttons : [ {
                                        text : '确定',
                                        handler : function() {
                                                var form = this.up('form').getForm();
                                                
                                                if(     this.up('form').down('combobox[name="job_type"]').getValue() == null ||
                                                        (Ext.String.trim(this.up('form').down('textfield[name="complete_count"]').getValue()) == ''
                                                                && Ext.String.trim(this.up('form').down('textfield[name="disuse_count"]').getValue()) == '')
                                                        )
                                                {
                                                        Ext.Msg.alert('完成结果','带*号资料必须输入');
                                                        return;
                                                }
                                                
                                                form.submit({
                                                        url : 'JobController?action=completeJob',
                                                        success : function(form, resp) {
                                                                Ext.Msg.alert('完成结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('jobStore').load();
                                                                win.close();
                                                        },
                                                        failure : function(form, resp) {
                                                                Ext.Msg.alert('完成结果', resp.result.msg);
                                                                Ext.data.StoreManager.lookup('jobStore').load();
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
        
        
        var selected = Ext.getCmp('job-grid').getSelectionModel().getSelection();
                if(selected.length == 0)
                {
                        Ext.Msg.alert("完成工作","请选择要完成的工作");
                        return;
                }
                else
                {
                        win.down('hiddenfield[name="id"]').setValue(selected[0].data.id);
                        win.down('textfield[name="complete_count"]').setValue(selected[0].data.remaining);
                        win.down('textfield[name="total"]').setValue(selected[0].data.remaining);
                        win.setTitle('完成工作 - ' + selected[0].data.product_name);
                        win.show();     
                }
    },
    
    
    //////////////////For mold module
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
        },
        onSummaryIncompleteTotalClicked : function()
        {
                Ext.data.StoreManager.lookup('jobStore').on('load', function countTotal() {
                var total = 0;
                Ext.data.StoreManager.lookup('jobStore').each(
                                function(item, index, totalItems ) {
                                        if(item.data ['status'] == "进行中")
                                                total += item.data ['remaining'];
                                }
                        );
                Ext.data.StoreManager.lookup('jobStore').removeListener('load', countTotal);
                Ext.Msg.alert("统计","未完成总数为 : " + total);
        });
        
        Ext.data.StoreManager.lookup('jobStore').load();
        }
});