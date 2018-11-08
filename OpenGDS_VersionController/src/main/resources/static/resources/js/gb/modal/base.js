/**
 * 모달 객체를 정의한다.
 * 
 * @class gb.modal.Base
 * @memberof gb.modal
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {String}
 *            obj.title - 모달의 제목
 * @param {Number}
 *            obj.width - 모달의 너비 (픽셀)
 * @param {Number}
 *            obj.height - 모달의 높이 (픽셀)
 * @param {Boolean}
 *            obj.autoOpen - 선언과 동시에 표출 할 것인지 선택
 * @param {Boolean}
 *            obj.keep - 모달 element를 생성시 미리 body에 append한다. true시 append된
 *            element를 css를 통해 보이거나 감춤, false시 open때마다 새롭게 body에 append한다.
 * @param {Function |
 *            String | DOM} obj.body - Modal 본문에 삽입될 내용
 * @param {Function |
 *            String | DOM} obj.footer- Modal 푸터에 삽입될 내용
 * @version 0.01
 * @author SOYIJUN
 * @date 2017. 07.26
 */
gb.modal.Base = function(obj) {
	var that = this;
	var options = obj ? obj : {};
	this.title = options.title ? options.title : "";
	this.width = options.width ? options.width : "auto";
	this.height = options.height ? options.height : "auto";
	this.keep = options.keep || false;
	this.autoOpen = options.autoOpen ? true : false;
	var span = $("<span>").html("&times;");
	var btn = $("<button>").append(span).click(function() {
		that.close();
	});
	this.titleArea = $("<span>").addClass("gb-modal-title").text(this.title);
	this.modalHead = $("<div>").addClass("gb-modal-head").append(this.titleArea).append(btn);
	var body = typeof options.body === "function" ? options.body() : options.body;
	var footer = typeof options.footer === "function" ? options.footer() : options.footer;
	this.modalBody = $("<div>").addClass("gb-modal-body");
	if (body) {
		$(this.modalBody).append(body);
	}
	this.modalFooter = $("<div>").addClass("gb-modal-footer");
	if (footer) {
		$(this.modalFooter).append(footer);
	}
// this.buttonArea = $("<span>").addClass("gb-modal-buttons");
// this.modalFooter =
// $("<div>").addClass("gb-modal-footer").append(this.buttonArea);
	this.modal = $("<div>").addClass("gb-modal").css({
		"width" : typeof this.width === "number" ? this.width+"px" : this.width,
				"height" : typeof this.height === "number" ? this.height+"px" : this.height,
						"position" : "absolute",
						"z-Index" : "999"
	}).append(this.modalHead).append(this.modalBody).append(this.modalFooter);

	this.background = $("<div>").addClass("gb-modal-background");
	if (this.keep) {
		$("body").append(this.modal);
		$("body").append(this.background);
	}
	if (this.autoOpen) {
		this.open();
	}
};
/**
 * 모달 바디를 반환한다.
 * 
 * @method gb.modal.Base#getModalBody
 * @return {DOM} 모달 본문에 해당하는 DOM
 */
gb.modal.Base.prototype.getModalBody = function() {
	return this.modalBody;
};
/**
 * 모달 바디를 설정한다.
 * 
 * @method gb.modal.Base#setModalBody
 * @param {DOM |
 *            function} body - 모달 본문으로 삽입될 내용
 */
gb.modal.Base.prototype.setModalBody = function(body) {
	if (typeof body === "function") {
		$(this.modalBody).append(body());
	} else {
		$(this.modalBody).append(body);
	}
};
/**
 * 모달 푸터를 반환한다.
 * 
 * @method gb.modal.Base#getModalFooter
 * @return {DOM} 모달 푸터에 해당하는 DOM
 */
gb.modal.Base.prototype.getModalFooter= function() {
	return this.modalFooter;
};
/**
 * 모달 푸터를 설정한다.
 * 
 * @method gb.modal.Base#setModalFooter
 * @param {DOM |
 *            function} footer - 모달 푸터로 삽입될 내용
 */
gb.modal.Base.prototype.setModalFooter = function(footer) {
	if (typeof footer === "function") {
		$(this.modalFooter).append(footer());
	} else {
		$(this.modalFooter).append(footer);
	}
};
/**
 * 모달을 반환한다.
 * 
 * @method gb.modal.Base#getModal
 * @return {DOM} 모달의 DOM
 */
gb.modal.Base.prototype.getModal = function() {
	return this.modal;
};
/**
 * 모달을 나타낸다.
 * 
 * @method gb.modal.Base#open
 */
gb.modal.Base.prototype.open = function() {
	if(!this.keep){
		$("body").append(this.background);
		$("body").append(this.modal);
	}
	var highz = this.getMaxZIndex();
	this.background.css({
		"display" : "block",
		"z-index" : highz + 1 
	});

	this.modal.css({
		"display" : "block",
		"z-index" : highz + 2 
	});
	this.refreshPosition();
};
/**
 * 모달을 숨긴다.
 * 
 * @method gb.modal.Base#close
 */
gb.modal.Base.prototype.close = function() {
	if(this.keep){
		this.background.css("display", "none");
		this.modal.css("display", "none");
	} else {
		this.background.detach();
		this.modal.detach();
	}
};
/**
 * 모달위치를 최신화한다.
 * 
 * @method gb.modal.Base#refreshPosition
 */
gb.modal.Base.prototype.refreshPosition = function() {
	$(this.modal).css({
		"top" : ($(window).innerHeight() / 2) - (this.getHeight()/2+50) + "px",
		"left" : ($(window).innerWidth() / 2) - (this.getWidth()/2) + "px"
	});
};
/**
 * 너비를 설정한다.
 * 
 * @method gb.modal.Base#setWidth
 * @param {Number}
 *            width - 모달의 너비(픽셀)
 */
gb.modal.Base.prototype.setWidth = function(width) {
	this.width = width;
	var value;
	if (typeof width === "number") {
		if (height > 0) {
			value = width + "px";
		} else {
			value = 0;
		}
	} else if (width === "auto"){
		value = "auto";
	}
	$(this.modal).css("width", value);
	this.refreshPosition();
};
/**
 * 너비를 반환한다.
 * 
 * @method gb.modal.Base#getWidth
 * @return {Number} 모달의 너비(픽셀)
 */
gb.modal.Base.prototype.getWidth = function() {
	return $(this.modal).outerWidth();
};
/**
 * 높이를 설정한다.
 * 
 * @method gb.modal.Base#setHeight
 * @param {Number}
 *            height - 모달의 높이(픽셀)
 */
gb.modal.Base.prototype.setHeight = function(height) {
	this.height = height;
	var value;
	if (typeof height === "number") {
		if (height > 0) {
			value = height + "px";
		} else {
			value = 0;
		}
	} else if (height === "auto"){
		value = "auto";
	}
	$(this.modal).css("height", value);
	this.refreshPosition();
};

/**
 * 높이를 반환한다.
 * 
 * @method gb.modal.Base#getHeight
 * @return {Number} 모달의 높이(픽셀)
 */
gb.modal.Base.prototype.getHeight = function() {
	return $(this.modal).outerHeight();
};

/**
 * 제일 높은 z index 값을 반환한다.
 * 
 * @method gb.modal.Base#getMaxZIndex
 * @return {Number} 모달의 높이(픽셀)
 */
gb.modal.Base.prototype.getMaxZIndex = function() {
	var maxZ = Math.max.apply(null,$.map($('body > div.gb-modal'), function(e,n){
		if($(e).css('position')=='absolute'){
			return parseInt($(e).css('z-index'))||1 ;
		}
	}));
	return maxZ;
};