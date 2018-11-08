/**
 * ol Map footer 객체를 정의한다.
 * footer를 생성하고 싶은 element를 인자값으로 받는다.
 * 해당 element는 반드시 relative position 이어야만 한다.
 * 
 * @author hochul.kim
 * @date 2018. 05.31
 * @version 0.01
 * @class gb.footer.Base
 * @constructor
 * @param {Object} obj (gb.footer 생성 기본 옵션)
 * @dependency jQuery
 */

var gb;
if (!gb)
	gb = {};
if (!gb.footer)
	gb.footer = {};

gb.footer.Base = function(obj) {
	var options = obj || {};
	
	/**
	 * 최상위 element
	 */
	this.footerTag = $("<footer>");
	
	this.contentTag = undefined;
	
	/**
	 * Default display
	 */
	this.isDisplay = options.isDisplay ? true : false;
	
	/**
	 * footer toggle event element class name
	 */
	this.toggleTarget = options.toggleTarget || ".footer-toggle-btn";
	
	/**
	 * footer를 생성할 element
	 */
	this.targetElement = options.targetElement;
	
	/**
	 * footer title
	 */
	this.title = options.title;
	
	/**
	 * footer content
	 */
	this.content = options.content || $("<h4>content area</h4>");
	
	/**
	 * element style 정의
	 */
	this.footerStyle = {
		"position": "absolute",
		"z-index": "3",
		"padding": "5px",
		"bottom": "0",
		"left": "0",
		"right": "0",
		"height": "30%",
		"background": "linear-gradient(to bottom, rgba(52,71,98,0.8) 0%,rgba(38,35,35,0.8) 100%)",
		"box-shadow": "0px 0px 20px rgba(0,0,0, 0.5)",
		"color": "#DDD"
	};
	
	this.titleAreaStyle = {
		"height": "20%",
		"padding": "8px 16px"
	};
	
	this.contentAreaStyle = {
		"height": "80%"
	};
	
	this.titleStyle = {
		"margin-top": "0"
	};
	
	this.createFooter({
		title: this.title,
		content: this.content
	});
	
	this.createToggleEvent(this.toggleTarget);
	
	if(!this.isDisplay || this.anotherFooterIsOpen()){
		this.close();
		this.isDisplay = false;
	}
}

gb.footer.Base.prototype.anotherFooterIsOpen = function(){
	var bool = false;
	var that = this;
	this.targetElement.find("footer").each(function(){
		if(that.footerTag[0] !== this){
			if($(this).css("display") === "block"){
				bool = true;
			}
		}
	});
	return bool;
}

gb.footer.Base.prototype.anotherFooterClose = function(){
	var that = this;
	this.targetElement.find("footer").each(function(){
		if(that.footerTag[0] !== this){
			if($(this).css("display") === "block"){
				$(this).css("display", "none");
			}
		}
	});
}


/**
 * element style 적용 함수
 * @method adjustStyle_
 * @param {n.fn.init} element (jQuery 선택자)
 * @param {Object} style (style정의 객체)
 * @private
 */
gb.footer.Base.prototype.adjustStyle_ = function(element, style){
	for(var content in style){
		element.css(content, style[content]);
	}
}

/**
 * footer layout을 생성한다
 * @method createContent
 * @param {Object} opt (footer Tag 내부에 정의할 element 정보)
 */
gb.footer.Base.prototype.createFooter = function(opt){
	
	var that = this;
	
	// footer Tag 초기화
	this.footerTag.empty();
	
	this.adjustStyle_(this.footerTag, this.footerStyle);
	
	var titleArea = $("<div class='footer-header'>");
	this.adjustStyle_(titleArea, this.titleAreaStyle);
	
	this.contentTag = $("<div class='footer-content'>");
	this.adjustStyle_(this.contentTag, this.contentAreaStyle);
	
	this.title = opt.title || "Title";
	
	this.titleTag = $("<h3>");
	this.adjustStyle_(this.titleTag, this.titleStyle);
	this.titleTag.text(this.title);
	
	titleArea.append(this.titleTag);
	this.footerTag.append(titleArea);
	
	if(!!opt.content){
		this.contentTag.append(opt.content);
		this.footerTag.append(this.contentTag);
	}
	
	this.targetElement.append(this.footerTag);
}

/**
 * footer를 나타낸다.
 * @method open
 */
gb.footer.Base.prototype.open = function(){
	this.footerTag.css("display", "block");
	this.footerTag.addClass("footer-open").trigger("footeropen");
}

/**
 * footer를 숨긴다
 * @method close
 */
gb.footer.Base.prototype.close = function(){
	this.footerTag.css("display", "none");
	this.footerTag.removeClass("footer-open").trigger("footerclose");
}

/**
 * footer를 open, close 하는 이벤트 함수를 생성한다.
 * @method createToggleEvent
 */
gb.footer.Base.prototype.createToggleEvent = function(target){
	var that = this;
	
	$(target).click(function(){
		if(that.footerTag.css("display") === "block"){
			that.close();
			that.isDisplay = false;
		} else {
			that.anotherFooterClose();
			that.open();
			that.isDisplay = true;
		}
	})
}

gb.footer.Base.prototype.setTitle = function(title){
	if(typeof title === "string"){
		this.title = title;
		this.titleTag.text(title);
	}
}