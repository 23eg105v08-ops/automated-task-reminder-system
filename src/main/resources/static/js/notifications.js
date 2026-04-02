(function () {
    const POLL_MS = 8000;
    let lastSeenId = 0;

    function ensureToastContainer() {
        let container = document.getElementById('toastContainer');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toastContainer';
            container.className = 'toast-container';
            document.body.appendChild(container);
        }
        return container;
    }

    function levelClass(level) {
        const normalized = (level || '').toUpperCase();
        if (normalized === 'OVERDUE' || normalized === 'HIGH') {
            return 'danger';
        }
        if (normalized === 'MEDIUM') {
            return 'warning';
        }
        return 'success';
    }

    function showToast(notification) {
        const container = ensureToastContainer();
        const toast = document.createElement('div');
        toast.className = 'toast toast-' + levelClass(notification.level);

        toast.innerHTML = [
            '<strong>' + (notification.level || 'UPDATE') + ' Alert</strong>',
            '<p>' + (notification.message || 'Task update received.') + '</p>'
        ].join('');

        container.appendChild(toast);

        setTimeout(function () {
            toast.classList.add('show');
        }, 10);

        setTimeout(function () {
            toast.classList.remove('show');
            setTimeout(function () {
                toast.remove();
            }, 250);
        }, 6500);
    }

    async function getLatestId() {
        const response = await fetch('/api/notifications/latest-id', { headers: { 'Accept': 'application/json' } });
        if (!response.ok) {
            return 0;
        }
        const payload = await response.json();
        return Number(payload.latestId || 0);
    }

    async function fetchNewNotifications() {
        const response = await fetch('/api/notifications/after?afterId=' + lastSeenId, {
            headers: { 'Accept': 'application/json' }
        });

        if (!response.ok) {
            return;
        }

        const notifications = await response.json();
        if (!Array.isArray(notifications) || notifications.length === 0) {
            return;
        }

        notifications.forEach(function (notification) {
            const id = Number(notification.id || 0);
            if (id > lastSeenId) {
                lastSeenId = id;
            }
            showToast(notification);
        });
    }

    async function start() {
        try {
            // Start from current latest event to avoid replaying old toasts on page load.
            lastSeenId = await getLatestId();
        } catch (error) {
            lastSeenId = 0;
        }

        setInterval(function () {
            fetchNewNotifications().catch(function () {
                // Ignore transient polling errors.
            });
        }, POLL_MS);
    }

    start().catch(function () {
        // Ignore startup errors.
    });
})();
