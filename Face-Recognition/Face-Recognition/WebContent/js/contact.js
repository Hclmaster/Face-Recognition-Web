$(function() {
    "use strict";
    var appendLi,
        slide,
        autoRun,
        interval_id,
        slider = $(".slider"),
        sliderUl = slider.find("ul"),
        sliderUlLi = sliderUl.find("li"),
        sliderOl = slider.find("ol"),
        sliderOlLi = sliderOl.find("li"),
        sectionDiv = slider.find("section > div");

    $(document).keydown(function(e) {
        if (e.keyCode == 39) {
            sliderUlLi.click();
        }
    });

    function changeBackground(elem) {
        $(".parent").css(
            "background-image",
            "url(" + elem.find("img").attr("src") + ")"
        );
    }

    function startSlider() {
        appendLi = sliderOl.find("li:first");
        sliderOl.append(appendLi);
    }
    startSlider();

    function gsapSlider(elem) {
        if (elem.hasClass("active")) {
            TweenMax.set(sectionDiv, { autoAlpha: 0 });
            TweenMax.set(elem, { autoAlpha: 1 });
            TweenMax.from(elem.find("h2, p"), 0.75, { x: -150, autoAlpha: 0 });
            TweenMax.from(elem.find("img"), 0.75, {
                scale: 0.05,
                autoAlpha: 0,
                rotation: 90,
                borderRadius: "100%"
            });
        }
        TweenMax.from(sliderOlLi, 0.75, { x: -100 });
    }
    function runSlider(elem) {
        elem.addClass("active").siblings("div").removeClass("active");
        startSlider();
        gsapSlider(elem);
        changeBackground(elem);
    }

    sliderUlLi.on("click", function() {
        /*slide = $(".slider section > div.active").is(":last-of-type")
            ? $(".slider section > div:first")
            : $(".slider section > div.active").next("div");
        runSlider(slide);*/
    	window.location.href="index.html";
    });

    sliderOlLi.on("click", function() {
        slide = $(".slider section > div." + $(this).data("title"));
        runSlider(slide);
    });

    
});
