(function () {
    const askButton = document.getElementById('askAiBtn');
    const questionInput = document.getElementById('aiQuestion');
    const answerBox = document.getElementById('aiAnswer');

    if (!askButton || !questionInput || !answerBox) {
        return;
    }

    askButton.addEventListener('click', async function () {
        const question = questionInput.value.trim();
        if (!question) {
            answerBox.style.display = 'block';
            answerBox.textContent = 'Please enter a question first.';
            return;
        }

        askButton.disabled = true;
        askButton.textContent = 'Thinking...';
        answerBox.style.display = 'block';
        answerBox.textContent = 'Generating answer...';

        try {
            const response = await fetch('/api/ai/ask', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ question: question })
            });

            if (!response.ok) {
                let message = 'AI request failed';
                const raw = await response.text();
                try {
                    const parsed = JSON.parse(raw);
                    message = parsed.message || parsed.error || raw || message;
                } catch (_) {
                    message = raw || message;
                }
                throw new Error(message);
            }

            const data = await response.json();
            answerBox.textContent = data.answer || 'No response received.';
        } catch (error) {
            answerBox.textContent = 'Error: ' + (error.message || 'Unable to fetch AI response');
        } finally {
            askButton.disabled = false;
            askButton.textContent = 'Ask AI';
        }
    });
})();
