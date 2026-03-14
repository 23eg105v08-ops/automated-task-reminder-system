(function () {
    async function postJson(url, body) {
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: body ? JSON.stringify(body) : '{}'
        });

        const raw = await response.text();
        let parsed;
        try {
            parsed = JSON.parse(raw);
        } catch (_) {
            parsed = { message: raw || 'No response body' };
        }

        if (!response.ok) {
            const msg = parsed.message || parsed.error || 'Request failed';
            throw new Error(msg);
        }

        return parsed;
    }

    function setResult(target, text, isError) {
        target.style.display = 'block';
        target.textContent = text;
        target.style.borderColor = isError ? 'rgba(241, 94, 108, 0.6)' : 'rgba(34, 197, 94, 0.5)';
    }

    const aiBtn = document.getElementById('testAiBtn');
    const aiInput = document.getElementById('testAiQuestion');
    const aiResult = document.getElementById('testAiResult');

    if (aiBtn && aiInput && aiResult) {
        aiBtn.addEventListener('click', async function () {
            aiBtn.disabled = true;
            aiBtn.textContent = 'Testing...';
            setResult(aiResult, 'Calling AI endpoint...', false);

            try {
                const data = await postJson('/api/integrations/test/ai', { question: aiInput.value.trim() });
                setResult(aiResult, (data.message || 'AI test completed') + '\n\n' + (data.answer || ''), false);
            } catch (error) {
                setResult(aiResult, 'AI test failed: ' + (error.message || 'Unknown error'), true);
            } finally {
                aiBtn.disabled = false;
                aiBtn.textContent = 'Test AI';
            }
        });
    }

    const smsBtn = document.getElementById('testSmsBtn');
    const smsResult = document.getElementById('testSmsResult');

    if (smsBtn && smsResult) {
        smsBtn.addEventListener('click', async function () {
            smsBtn.disabled = true;
            smsBtn.textContent = 'Testing...';
            setResult(smsResult, 'Triggering SMS test...', false);

            try {
                const data = await postJson('/api/integrations/test/sms');
                setResult(smsResult, data.message || 'SMS test completed', data.status !== 'ok');
            } catch (error) {
                setResult(smsResult, 'SMS test failed: ' + (error.message || 'Unknown error'), true);
            } finally {
                smsBtn.disabled = false;
                smsBtn.textContent = 'Test SMS';
            }
        });
    }

    const emailBtn = document.getElementById('testEmailBtn');
    const emailResult = document.getElementById('testEmailResult');

    if (emailBtn && emailResult) {
        emailBtn.addEventListener('click', async function () {
            emailBtn.disabled = true;
            emailBtn.textContent = 'Testing...';
            setResult(emailResult, 'Triggering overdue email test...', false);

            try {
                const data = await postJson('/api/integrations/test/email-overdue');
                setResult(emailResult, data.message || 'Email test completed', data.status !== 'ok');
            } catch (error) {
                setResult(emailResult, 'Email test failed: ' + (error.message || 'Unknown error'), true);
            } finally {
                emailBtn.disabled = false;
                emailBtn.textContent = 'Test Overdue Email';
            }
        });
    }
})();
