$(document).ready(function () {
    $('button.on-top').hover(
        function () {
            $('button.underneath').css('opacity', '0.5');
        }, function () {
            $('button.underneath').css('opacity', '0');
        }
    );
});