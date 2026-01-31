// DARK MODE

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

function toggleDark() {
    document.body.classList.toggle("dark");
    const darkToggle = document.querySelector(".dark-toggle i");
    const isDark = document.body.classList.contains("dark");

    localStorage.setItem("dark", isDark);

    if (darkToggle) {
        if (isDark) {
            darkToggle.classList.remove("fa-moon");
            darkToggle.classList.add("fa-sun");
        } else {
            darkToggle.classList.remove("fa-sun");
            darkToggle.classList.add("fa-moon");
        }
    }
}

// TOAST

function showToast(message, isError = false) {
    const toast = document.getElementById("toast");
    if (!toast) return;

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

//  NOTIFICATION SOUND

const notificationSound = new Audio("/sounds/message pop alert.mp3");

// SEND MONEY MODAL

const modal = document.getElementById("confirmModal");
const confirmText = document.getElementById("confirmText");
const confirmYes = document.getElementById("confirmYes");
const confirmNo = document.getElementById("confirmNo");

function closeModal() {
    if (modal) modal.style.display = "none";
}

window.onclick = function (event) {
    if (event.target === modal) closeModal();
};

// SEND MONEY FORM (SAFE)

const sendMoneyForm = document.getElementById("sendMoneyForm");
if (sendMoneyForm) {
    sendMoneyForm.addEventListener("submit", async e => {
        e.preventDefault();

        const form = e.target;
        const data = new FormData(form);
        const receiverPhone = data.get("receiverPhone");
        const amount = data.get("amount");

        let fullName = "";
        try {
            const res = await fetch(`/api/v1/users/byPhone?phone=${encodeURIComponent(receiverPhone)}`);
            if (!res.ok) {
                showToast("Receiver not found", true);
                return;
            }
            const receiver = await res.json();
            fullName = receiver.fullName;
        } catch {
            showToast("Failed to fetch receiver info", true);
            return;
        }

        if (confirmText && modal) {
            confirmText.innerText =
                `Are you sure you want to send KES ${amount} to ${fullName} (${receiverPhone})?`;
            modal.style.display = "block";
        }

        if (confirmYes) {
            confirmYes.onclick = async () => {
                closeModal();
                try {
                    const response = await fetch("/sendMoney", {
                        method: "POST",
                        body: data
                    });
                    const msg = await response.text();
                    if (!response.ok) throw new Error(msg);

                    showToast(msg);
                    form.reset();
                    if (typeof getBalance === "function") getBalance();
                } catch (err) {
                    showToast(err.message, true);
                }
            };
        }

        if (confirmNo) {
            confirmNo.onclick = closeModal;
        }
    });
}

// WEBSOCKET

let stompClient = null;

function connectSocket() {
    const socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, () => {

        stompClient.subscribe("/user/queue/notifications", msg => {
            const data = JSON.parse(msg.body);

            showNotificationToast(data.message);

            notificationSound.play().catch(() => {});

            const bal = document.getElementById("balanceValue");
            if (bal && data.balance !== undefined) {
                bal.innerText = data.balance;
            }

            incrementNotifBadge();
        });
    });
}

// NOTIFICATION BADGE

function incrementNotifBadge() {
    const badge = document.getElementById("notifCount");
    if (!badge) return;

    const current = parseInt(badge.innerText || "0", 10);
    badge.innerText = current + 1;
    badge.style.display = "inline-block";
}

// INIT

document.addEventListener("DOMContentLoaded", connectSocket);

function openNotifications() {
    window.location.href = "/notifications";
}