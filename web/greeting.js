const form = document.querySelector("form");
const nameField = document.querySelector("#name");
const styleField = document.querySelector("#style");
const submit = form.querySelector("button");
const result = document.querySelector("#result");
const copy = document.querySelector("#copy");
const feedback = document.querySelector("#feedback");
const history = document.querySelector("#history");
const entries = history.querySelector("ol");

const greetings = {
    casual: (name) => name + "님, 반가워요! 오늘도 좋은 하루 보내요.",
    formal: (name) => name + "님, 안녕하십니까. 평안한 하루 되시길 바랍니다."
};

function showResult(message, isError) {
    result.textContent = message;
    result.dataset.error = String(isError);
    copy.hidden = isError;
    if (!matchMedia("(prefers-reduced-motion: reduce)").matches) {
        result.getAnimations().forEach((animation) => animation.cancel());
        result.animate(
            [{ opacity: .45, transform: "translateY(6px)" }, { opacity: 1, transform: "none" }],
            { duration: 240, easing: "ease-out" }
        );
    }
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (submit.disabled) return;

    const name = nameField.value.trim();
    const style = styleField.value;
    feedback.textContent = "";
    copy.hidden = true;
    if (!name) {
        showResult("이름을 입력해 주세요.", true);
        nameField.focus();
        return;
    }
    submit.disabled = true;
    submit.textContent = "만드는 중…";
    try {
        const response = await fetch("./api/greeting", { method: "POST", body: name });
        const text = await response.text();
        if (!response.ok) {
            showResult(response.status === 400 || response.status === 413
                ? text
                : "인사를 만들 수 없습니다. 다시 시도해 주세요.", true);
            return;
        }

        const message = greetings[style]?.(name) ?? text;
        showResult(message, false);
        const entry = document.createElement("li");
        entry.textContent = message;
        entries.prepend(entry);
        if (entries.children.length > 5) entries.lastElementChild.remove();
        history.hidden = false;
    } catch {
        showResult("서버에 연결할 수 없습니다. 다시 시도해 주세요.", true);
    } finally {
        submit.disabled = false;
        submit.textContent = "인사하기";
    }
});

copy.addEventListener("click", async () => {
    try {
        await navigator.clipboard.writeText(result.textContent);
        if (!copy.hidden) feedback.textContent = "복사했습니다.";
    } catch {
        if (!copy.hidden) feedback.textContent = "인사말을 선택해 직접 복사해 주세요.";
    }
});
