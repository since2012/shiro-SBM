<!DOCTYPE HTML>
<html>
<head>
    <title>新增库存</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta charset="UTF-8">

    <!-- Bootstrap 3.3.7 -->
    <link rel="stylesheet" href="/static/bower_components/bootstrap/dist/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="/static/bower_components/font-awesome/css/font-awesome.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="/static/dist/css/AdminLTE.css">
    <!-- jQuery 3 -->
    <script src="/static/bower_components/jquery/dist/jquery.min.js"></script>
    <!-- Bootstrap 3.3.7 -->
    <script src="/static/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- bootstrap datetimepicker -->
    <link rel="stylesheet" href="/static/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
    <script src="/static/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/static/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" href="/static/plugins/jqueryZtree/zTreeStyle.css">
    <script src="/static/plugins/jqueryZtree/jquery.ztree.all.min.js"></script>
    <script src="/static/plugins/common/ztree-object.js"></script>

    <link rel="stylesheet" href="/static/plugins/bootstrapValidator/bootstrapValidator.min.css">
    <script src="/static/plugins/bootstrapValidator/bootstrapValidator.min.js"></script>

    <script src="/static/plugins/common/ajax-object.js"></script>
    <script src="/static/plugins/common/Feng.js"></script>
    <script src="/static/modular/biz/coupon/coupon_info.js"></script>
</head>
<body>
<!-- Main content -->
<section class="content">
    <div class="box">
        <div class="form-horizontal bv-form" id="couponInfoForm">
            <input type="hidden" id="id" value="">
            <div class="row">
                <div class="col-xs-6 b-r">
                    <div class="form-group has-feedback">
                        <label class="col-xs-3 control-label">卖家</label>
                        <div class="col-xs-9">
                            <input class="form-control" id="sellername" name="sellername" type="text"
                                   readonly="readonly"
                                   onclick="CouponInfo.showSellerNameSelectTree(); return false;"
                                   style="background-color: #ffffff !important;">
                            <input class="form-control" type="hidden" id="sellerid" value="">
                            <!-- 父级部门的选择框 -->
                            <div id="sellerMenu" class="menuContent"
                                 style="display: none; position: absolute; z-index: 200;">
                                <ul id="sellerTree" class="ztree tree-box" style="width: 245px !important;">
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-3 control-label">开始日期</label>
                        <div class="col-xs-9">
                            <div class="input-group date form_date" data-date=""
                                 data-date-format="yyyy-mm-dd" data-link-field="beginday"
                                 data-link-format="yyyy-mm-dd">
                                <input class="form-control" size="16" type="text" value=""
                                       readonly>
                                <span class="input-group-addon"><span
                                        class="glyphicon glyphicon-remove"></span></span>
                                <span class="input-group-addon"><span
                                        class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                            <input type="hidden" id="beginday" value=""/>
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div id="driverInfoContent">
                        <div class="form-group has-feedback">
                            <label class="col-xs-3 control-label">数量</label>
                            <div class="col-xs-9">
                                <input class="form-control" id="total" name="total" type="text">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label">结束日期</label>
                            <div class="col-xs-9">
                                <div class="input-group date form_date" data-date=""
                                     data-date-format="yyyy-mm-dd" data-link-field="endday"
                                     data-link-format="yyyy-mm-dd">
                                    <input class="form-control" size="16" type="text" value=""
                                           readonly>
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-remove"></span></span>
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                                <input type="hidden" id="endday" value=""/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row btn-group-m-t">
                <div class="text-center">
                    <button type="button" class="btn btn-info " onclick="CouponInfo.addSubmit()" id="ensure">
                        <i class="fa fa-check"></i>&nbsp;提交
                    </button>
                    <button type="button" class="btn btn-danger " onclick="CouponInfo.close()" id="cancel">
                        <i class="fa fa-eraser"></i>&nbsp;取消
                    </button>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>