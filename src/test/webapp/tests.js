QUnit.test('createPayload wraps prompt', function(assert) {
    var p = createPayload('hello');
    assert.equal(p.prompt, 'hello');
});

QUnit.test('preparePrompts replaces placeholder', function(assert) {
    var arr = preparePrompts('Say [REQUEST]', 'A\nB');
    assert.deepEqual(arr, ['Say A', 'Say B']);
});

QUnit.test('collectResponses joins llama messages', function(assert) {
    var msgs = [
        { from: 'You', text: 'hi' },
        { from: 'Llama', text: 'one' },
        { from: 'Llama', text: 'two' }
    ];
    assert.equal(collectResponses(msgs), 'one\ntwo');
});
