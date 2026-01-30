if (localStorage.getItem("dark") === "true") {
    document.body.classList.add("dark");
}

if (document.body.classList.contains("dark")) {
    const icon = document.querySelector(".dark-toggle i");
    if (icon) {
        icon.classList.remove("fa-moon");
        icon.classList.add("fa-sun");
    }
}

const notificationSound = new Audio("/sounds/message pop alert.mp3");


function toggleDark() {
    document.body.classList.toggle("dark");
    const darkToggle = document.querySelector(".dark-toggle i");
    const isDark = document.body.classList.contains("dark");

    localStorage.setItem("dark", isDark);

    if (isDark) {
        darkToggle.classList.remove("fa-moon");
        darkToggle.classList.add("fa-sun");
    } else {
        darkToggle.classList.remove("fa-sun");
        darkToggle.classList.add("fa-moon");
    }
}

function showToast(message, isError = false) {
            const toast = document.getElementById("toast");
            toast.innerText = message;
            toast.className = "toast show" + (isError ? " error" : "");

            setTimeout(() => toast.classList.remove("show"), 3000);
}

function showNotificationToast(message, isError = false) {
    const container = document.getElementById("notif-toast-container");
    if (!container) return;

    const toast = document.createElement("div");
    toast.className = "notif-toast" + (isError ? " error" : "");
    toast.innerText = message;

    container.appendChild(toast);

    setTimeout(() => toast.classList.add("show"), 50);

    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => container.removeChild(toast), 300);
    }, 10000);
}

const modal = document.getElementById("confirmModal");
const confirmText = document.getElementById("confirmText");
const confirmYes = document.getElementById("confirmYes");
const confirmNo = document.getElementById("confirmNo");

function closeModal() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target === modal) closeModal();
}

document.getElementById("sendMoneyForm").addEventListener("submit", async e => {
    e.preventDefault(); // stop default form submit

    const form = e.target;
    const data = new FormData(form);
    const receiverPhone = data.get("receiverPhone");
    const amount = data.get("amount");

    let fullName = "";
    try {
        const res = await fetch(`/api/v1/users/byPhone?phone=${encodeURIComponent(receiverPhone)}`);
        if (res.ok) {
            const receiver = await res.json();
            fullName = receiver.fullName;
        } else {
            showToast("Receiver not found", true);
            return;
        }
    } catch {
        showToast("Failed to fetch receiver info", true);
        return;
    }

    confirmText.innerText = `Are you sure you want to send KES ${amount} to ${fullName} (${receiverPhone})?`;
    modal.style.display = "block";

    confirmYes.onclick = async () => {
        modal.style.display = "none";

        try {
            const response = await fetch("/sendMoney", { method: "POST", body: data });
            const msg = await response.text();
            if (!response.ok) throw new Error(msg);
            showToast(msg);
            form.reset();
            getBalance();
        } catch (err) {
            showToast(err.message, true);
        }
    };

    confirmNo.onclick = closeModal;
});

let stompClient = null;

function connectSocket() {
    const socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.debug = null; // silence logs

    stompClient.connect({}, () => {

        stompClient.subscribe("/user/queue/notifications", msg => {
            const data = JSON.parse(msg.body);

            showNotificationToast(data.message);

                notificationSound.play().catch(err => {
                    console.warn("Notification sound could not play:", err);
                });

            const bal = document.getElementById("balanceValue");
            if (bal && data.balance !== undefined) {
                bal.innerText = data.balance;
            }

            incrementNotifBadge();
        });
    });
}

function incrementNotifBadge() {
    const badge = document.getElementById("notifCount");
    if (!badge) return;

    const current = parseInt(badge.innerText || "0");
    badge.innerText = current + 1;
    badge.style.display = "inline-block";
}

document.addEventListener("DOMContentLoaded", connectSocket);

function openNotifications() {
    window.location.href = "/notifications";
}