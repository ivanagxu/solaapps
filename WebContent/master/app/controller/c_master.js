var SESSION_USER;
window.onload = function()
{
	
};

function HAS_PERMISSION(role, alert) {
	if (alert == undefined)
		alert = true;

	if (!SESSION_USER) {
		if (alert) {
			Ext.Msg.alert('System', "没有权限");
		}
		return false;
	} else {
		try {
			var roles = SESSION_USER.post.role;
			for ( var i = 0; i < roles.length; i++) {
				if (roles[i].name == role || roles[i].name == '管理员' || roles[i].name == '经理' ||
					roles[i].name == '厂长')
					return true;
			}
			if (alert) {
				Ext.Msg.alert('System', "没有权限");
			}
			return false;
		} catch (e) {
			return false;
		}
	}
}

function GOTO_ORDER_MODULE() {
	hasPermission = HAS_PERMISSION("销售部");
	if (hasPermission) {
		location.href = "order.jsp";
	}
}

function GOTO_JOB_MODULE() {
	hasPermission = true;
	if (hasPermission) {
		location.href = "job.jsp";
	}
}

function GOTO_ADMIN_MODULE() {
	hasPermission = HAS_PERMISSION("管理员");
	if (hasPermission) {
		location.href = "admin.jsp";
	}
}

function GOTO_INVENTORY_MODULE() {
	hasPermission = HAS_PERMISSION("销售部", false) || HAS_PERMISSION("生产部", false);
	if (hasPermission) {
		location.href = "inventory.jsp";
	}
	else
	{
		Ext.Msg.alert('System', "没有权限");
	}
}

function GOTO_DOCUMENT_MODULE() {
	hasPermission = true;
	if (hasPermission) {
		location.href = "document.jsp";
	} 
}

function GOTO_PRODUCTION_MODULE() {
	hasPermission = HAS_PERMISSION("生产部");
	if (hasPermission) {
		location.href = "production.jsp";
	} 
}

function GOTO_PRODUCTIONLOG_MODULE() {
	hasPermission = true;
	if (hasPermission) {
		location.href = "productionlog.jsp";
	} 
}

function LOGOUT()
{
	location.href="UserACController?action=logout";
}

function GET_HEIGHT() {
	  var myWidth = 0, myHeight = 0;
	  if( typeof( window.innerWidth ) == 'number' ) {
	//Non-IE
	    myWidth = window.innerWidth;
	    myHeight = window.innerHeight;
	  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
	    //IE 6+ in 'standards compliant mode'
	    myWidth = document.documentElement.clientWidth;
	    myHeight = document.documentElement.clientHeight;
	  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
	    //IE 4 compatible
	    myWidth = document.body.clientWidth;
	    myHeight = document.body.clientHeight;
	  }
	  //window.alert( 'Width = ' + myWidth );
	  //window.alert( 'Height = ' + myHeight );
	  return myHeight - 130 -  Math.random() * 20;
}

function GET_WIDTH() {
	  var myWidth = 0, myHeight = 0;
	  if( typeof( window.innerWidth ) == 'number' ) {
	//Non-IE
	    myWidth = window.innerWidth;
	    myHeight = window.innerHeight;
	  } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
	    //IE 6+ in 'standards compliant mode'
	    myWidth = document.documentElement.clientWidth;
	    myHeight = document.documentElement.clientHeight;
	  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
	    //IE 4 compatible
	    myWidth = document.body.clientWidth;
	    myHeight = document.body.clientHeight;
	  }
	  //window.alert( 'Width = ' + myWidth );
	  //window.alert( 'Height = ' + myHeight );
	  return myWidth;
}

function DISPLAY_IMAGE_WINDOW(image,drawing)
{
	var win = Ext.create('Ext.window.Window', {
		title : '图片',
		height : GET_HEIGHT(),
		width : GET_WIDTH() * 0.8,
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
				anchor : '100%'
			},
			items : [
	     	{
	    		id : 'image-in-window',
	    		html: '<img src="" height=1600 width=1280 />'
	    	}]
		}
		)]
	}
	);
	
	win.show();
	Ext.getCmp('image-in-window').update('产品图片<br/><img src="'+ image + '" height=50%/><br/>' + 
			'产品图纸<br/><img src="'+ drawing + '" height=50%/>');
}


function GetJsonData(sUrl, xParameter, callback) {
	Ext.Ajax.request({
		url : sUrl,
		params : xParameter,
		success : function(response) {
			var jsonResp = Ext.JSON.decode(response.responseText);
			callback(jsonResp);
		},
		failure : function(response) {
			var jsonResp = Ext.JSON.decode(response.responseText);
			callback(jsonResp);
		}
	});
}

Ext.define('master.controller.c_master', {
	extend : 'Ext.app.Controller',

	views : [ 'v_master' ],

	init : function() {
		//Product Data
		Ext.define('ProductData', {
			extend : 'Ext.data.Model',
			fields : [ 'name', 'name_eng', 'our_name', 'status', 'image',
					'drawing', 'mold_rate', 'machining_pos', 'handwork_pos',
					'polishing' , 'finished', 'semi_finished','mold_code', 'mold_name', 'mold_stand_no','image', 'drawing']
		});

		//Order Data
		Ext.define('OrderData', {
			extend : 'Ext.data.Model',
			fields : [ 'id', 'number', 'creator', 'product_name',
					'requirement_1', 'requirement_2', 'requirement_3',
					'requirement_4', 'create_date', 'deadline', 'status' ,
					'product_our_name', 'quantity', 'use_finished', 'use_semi_finished', 'customer_name', 
					'customer_code', 'product_rate', 'e_quantity', 'c_deadline', 'priority', 'image', 'drawing']
		});
		
		//Job Data
		Ext.define('JobData', {
			extend : 'Ext.data.Model',
			fields : [ 'order_id', 'id', 'number', 'order_user', 'customer_name' , 'customer_code', 'product_name', 'product_our_name'
			           , 'requirement1', 'requirement2', 'total', 'order_c_deadline', 'order_deadline', 'start_date','complete_date'
			           , 'section', 'order_status', 'order_remark', 'isNew', 'finished', 'status', 'handled_by', 
			           'product_image', 'product_drawing', 'finish_remark', 'remaining', 'total_rejected', 'assigned_to', 'priority', 'image', 'drawing']
		});

		//JobType Data
		Ext.define('JobTypeData', {
			extend : 'Ext.data.Model',
			fields : [ 'name', 'role', 'status', 'remark' ]
		});
		
		//Customer Data
		Ext.define('CustomerData', {
			extend : 'Ext.data.Model',
			fields : ['id','name', 'code', 'phone','fax','email','contact_person']
		});
		
		//UserAC Data
		Ext.define('UserACData', {
			extend : 'Ext.data.Model',
			fields : ['id','login_id', 'name', 'password','post','department','division',
			          'section', 'email', 'sex', 'birthday', 'employ_date', 'create_date', 'update_date',
			          'salary', 'status', 'role']
		});
		
		//Section Data
		Ext.define('SectionData', {
			extend : 'Ext.data.Model',
			fields : ['id','name','remark']
		});
		
		//Role Data
		Ext.define('RoleData', {
			extend : 'Ext.data.Model',
			fields : ['id','name','remark']
		});
		
		//Mold Data
		Ext.define('MoldData', {
			extend : 'Ext.data.Model',
			fields : ['code','name','stand_no']
		});
		
		//ProductRate Data
		Ext.define('ProductRate', {
			extend : 'Ext.data.Model',
			fields : ['order_number','product_name', 'product_our_name', 'rate', 'remark']
		});
		
		//ProductionLog Data
		Ext.define('ProductionLogData', {
			extend : 'Ext.data.Model',
			fields : ['product_name','product_our_name', 'finished', 'rejected', 'disuse', 'orders', 'deadlines', 'image', 'drawing']
		});
		
		//Document Data
		Ext.define('DocumentData', {
			extend : 'Ext.data.Model',
			fields : ['name','type', 'full_path']
		});
		
		//All role store
		Ext.create('Ext.data.Store', {
			storeId : 'allMoldStore',
			model : 'MoldData',
			proxy : {
				type : 'ajax',
				url : 'MoldController?action=getAllMold',
				reader : {
					type : 'json',
					root : 'data',
					model : 'MoldData'
				}
			}
		});
		
		//All role store
		Ext.create('Ext.data.Store', {
			storeId : 'allRoleStore',
			model : 'RoleData',
			proxy : {
				type : 'ajax',
				url : 'RoleController?action=getAllRole',
				reader : {
					type : 'json',
					root : 'data',
					model : 'RoleData'
				}
			}
		});
		
		//All section store
		Ext.create('Ext.data.Store', {
			storeId : 'allSectionStore',
			model : 'SectionData',
			proxy : {
				type : 'ajax',
				url : 'SectionController?action=getAllSection',
				reader : {
					type : 'json',
					root : 'data',
					model : 'SectionData'
				}
			}
		});

		//Valid job type store
		Ext.create('Ext.data.Store', {
			storeId : 'jobTypeStore',
			model : 'JobTypeData',
			proxy : {
				type : 'ajax',
				url : 'JobTypeController?action=getValidJobType',
				reader : {
					type : 'json',
					root : 'data',
					model : 'JobTypeData'
				}
			}
		});

		//Valid product store
		Ext.create('Ext.data.Store', {
			storeId : 'productStore',
			model : 'ProductData',
			proxy : {
				type : 'ajax',
				url : 'ProductController?action=getValidProduct',
				reader : {
					type : 'json',
					root : 'data',
					model : 'ProductData'
				}
			}
		});

		//All product store
		Ext.create('Ext.data.Store', {
			storeId : 'allProductStore',
			model : 'ProductData',
			proxy : {
				type : 'ajax',
				url : 'ProductController?action=getAllProduct',
				reader : {
					type : 'json',
					root : 'data',
					model : 'ProductData'
				}
			}
		});

		//All customer store
		Ext.create('Ext.data.Store', {
			storeId : 'allCustomerStore',
			model : 'CustomerData',
			proxy : {
				type : 'ajax',
				url : 'CustomerController?action=getAllCustomer',
				reader : {
					type : 'json',
					root : 'data',
					model : 'CustomerData'
				}
			}
		});
		
		//All user store
		Ext.create('Ext.data.Store', {
			storeId : 'allUserACStore',
			model : 'UserACData',
			proxy : {
				type : 'ajax',
				url : 'UserACController?action=getAllUser',
				reader : {
					type : 'json',
					root : 'data',
					model : 'UserACData'
				}
			}
		});
		
		//All document store
		Ext.create('Ext.data.Store', {
			storeId : 'documentStore',
			model : 'DocumentData',
			proxy : {
				type : 'ajax',
				url : 'DocumentController?action=getAllDocument',
				reader : {
					type : 'json',
					root : 'data',
					model : 'DocumentData'
				}
			}
		});
	}
});
