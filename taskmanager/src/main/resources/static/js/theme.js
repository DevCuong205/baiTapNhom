document.addEventListener("DOMContentLoaded", function () {

    const body = document.body;
    const btn = document.getElementById("themeBtn");

    // Khôi phục theme
    if (localStorage.getItem("theme") === "dark") {
        body.classList.add("dark");
    }

    if (btn) {

        updateIcon();

        btn.addEventListener("click", function () {

            body.classList.toggle("dark");

            localStorage.setItem(
                "theme",
                body.classList.contains("dark") ? "dark" : "light"
            );

            updateIcon();

        });

    }

    function updateIcon() {

        if (!btn) return;

        btn.innerHTML =
                body.classList.contains("dark")
                        ? '<i class="fa-solid fa-sun"></i>'
                        : '<i class="fa-solid fa-moon"></i>';

    }

});