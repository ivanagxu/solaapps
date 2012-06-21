Ext.define('productionlog.controller.c_productionlog', {
        extend : 'Ext.app.Controller',

        views : [ 'v_productionlog' ],


        init : function() {
                this.control({
                        'panel[id=productlog-grid]' : {
                                render : this.onProductLogGridRender
                        },
                        'datefield[name=date]' : {
                                select : this.getProductLog
                        },
                        'datefield[name=end_date]' : {
                                select : this.getProductLog
                        },
                        'combobox[name=job_type]' : {
                                select : this.getProductLog
                        },
                        'button[text=刷新记录]' : {
                                click : this.getProductLog
                        },
                        'button[text=汇出记录]' : {
                                click : this.downloadProductLog
                        }
                });
        },
        
        onProductLogGridRender : function()
        {
                Ext.data.StoreManager.lookup('jobTypeStore').load();
        },
        
        getProductLog : function()
        {
                Ext.data.StoreManager.lookup('productionLogStore').proxy.url = 
                        'ReportController?action=generateProductLogReportByDateAndSection&date=' + Ext.getCmp('productlog_date').getRawValue() +
                        '&end_date=' + Ext.getCmp('productlog_end_date').getRawValue() +
                        '&job_type=' + encodeURI(Ext.getCmp('productlog_section').getValue());
                Ext.data.StoreManager.lookup('productionLogStore').load();
        },
        downloadProductLog : function()
        {
                window.open(
                                'ReportController?action=generateProductLogCSVReportByDate&date=' + Ext.getCmp('productlog_date').getRawValue() +
                                '&end_date=' + Ext.getCmp('productlog_end_date').getRawValue() +
                                '&job_type=' + encodeURI(Ext.getCmp('productlog_section').getValue())
                                );
        }
});