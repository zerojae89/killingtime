jQuery(function ($) {
    const $form = $("form");
    const $name = $("#name");
    const $style = $("#style");
    const $submit = $form.find('button[type="submit"]');
    const $result = $("#result");
    const $copy = $("#copy");
    const $feedback = $("#feedback");
    const $history = $("#history");
    const $entries = $history.find("ol");

    const greetings = {
        casual: (name) => name + "님, 반가워요! 오늘도 좋은 하루 보내요.",
        formal: (name) => name + "님, 안녕하십니까. 평안한 하루 되시길 바랍니다."
    };

    function showResult(message, isError) {
        $result.text(message).attr("data-error", String(isError));
        $copy.prop("hidden", isError);
        if (!matchMedia("(prefers-reduced-motion: reduce)").matches) {
            const result = $result[0];
            result.getAnimations().forEach((animation) => animation.cancel());
            result.animate(
                [{ opacity: .45, transform: "translateY(6px)" }, { opacity: 1, transform: "none" }],
                { duration: 240, easing: "ease-out" }
            );
        }
    }

    $form.on("submit", function (event) {
        event.preventDefault();
        if ($submit.prop("disabled")) return;

        const name = $name.val().trim();
        const style = $style.val();
        $feedback.empty();
        $copy.prop("hidden", true);
        if (!name) {
            showResult("이름을 입력해 주세요.", true);
            $name.trigger("focus");
            return;
        }

        $submit.prop("disabled", true).text("만드는 중…");
        $.ajax({
            url: "./api/greeting",
            method: "POST",
            data: name,
            contentType: "text/plain; charset=UTF-8",
            dataType: "text"
        }).done(function (text) {
            const message = greetings[style]?.(name) ?? text;
            showResult(message, false);
            $entries.prepend($("<li>").text(message));
            $entries.children().slice(5).remove();
            $history.prop("hidden", false);
        }).fail(function (response) {
            let message = "인사를 만들 수 없습니다. 다시 시도해 주세요.";
            if (response.status === 400 || response.status === 413) {
                message = response.responseText;
            } else if (response.status === 0) {
                message = "서버에 연결할 수 없습니다. 다시 시도해 주세요.";
            }
            showResult(message, true);
        }).always(function () {
            $submit.prop("disabled", false).text("인사하기");
        });
    });

    $copy.on("click", async function () {
        try {
            await navigator.clipboard.writeText($result.text());
            if (!$copy.prop("hidden")) $feedback.text("복사했습니다.");
        } catch {
            if (!$copy.prop("hidden")) $feedback.text("인사말을 선택해 직접 복사해 주세요.");
        }
    });
});
