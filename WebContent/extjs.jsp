<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="extjs/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="extjs/ux/grid/css/GridFilters.css" />

<script type="text/javascript" src="extjs/ext-all.js"></script>

<script type="text/javascript">
	Ext.onReady(function(){
		Ext.Loader.setConfig({enabled:true});
		
		Ext.Loader.setPath('Ext.ux', 'extjs/ux');
		Ext.require([
		    'Ext.ux.grid.FiltersFeature'
		]);
	});
</script>

