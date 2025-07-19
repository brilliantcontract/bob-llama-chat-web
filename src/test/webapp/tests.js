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

QUnit.test('collectChat joins all messages', function(assert) {
    var msgs = [
        { from: 'You', text: 'hi' },
        { from: 'Llama', text: 'hey' }
    ];
    assert.equal(collectChat(msgs), 'You: hi\nLlama: hey');
});

QUnit.test('runSequentially processes prompts sequentially', function(assert) {
    var done = assert.async();
    var calls = [];
    var d1 = $.Deferred();
    var d2 = $.Deferred();
    function sendFn(p) {
        calls.push(p);
        return calls.length === 1 ? d1 : d2;
    }
    var progress = [];
    runSequentially(['A', 'B'], sendFn, function(c, t) { progress.push(c + '/' + t); }, function() { return false; }, function() {
        assert.deepEqual(calls, ['A', 'B']);
        assert.deepEqual(progress, ['1/2', '2/2']);
        done();
    });
    assert.deepEqual(calls, ['A']);
    d1.resolve();
    setTimeout(function() {
        assert.deepEqual(calls, ['A', 'B']);
        d2.resolve();
    }, 0);
});

QUnit.test('runSequentially stops when interrupted', function(assert) {
    var done = assert.async();
    var calls = [];
    var d1 = $.Deferred();
    function sendFn(p) {
        calls.push(p);
        return d1;
    }
    var cancel = false;
    runSequentially(['A', 'B'], sendFn, null, function() { return cancel; }, function() {
        assert.deepEqual(calls, ['A']);
        done();
    });
    d1.resolve();
    cancel = true;
});

QUnit.test('checkLlama uses GET', function(assert) {
    var orig = $.get;
    var called = null;
    $.get = function(url) { called = url; return $.Deferred(); };
    checkLlama();
    assert.equal(called, 'ping');
    $.get = orig;
});

QUnit.test('listModels uses GET', function(assert) {
    var orig = $.get;
    var called = null;
    $.get = function(url) { called = url; return $.Deferred(); };
    listModels();
    assert.equal(called, 'models');
    $.get = orig;
});

QUnit.test('pullLlama3 uses POST', function(assert) {
    var orig = $.post;
    var called = null;
    var data = null;
    $.post = function(url, payload) { called = url; data = payload; return $.Deferred(); };
    pullLlama3();
    assert.equal(called, 'pull');
    assert.deepEqual(data, { name: 'llama3:8b' });
    $.post = orig;
});

QUnit.test('formatData converts objects to string', function(assert) {
    var obj = { a: 1 };
    assert.equal(formatData(obj), JSON.stringify(obj, null, 2));
});

QUnit.test('formatData leaves strings unchanged', function(assert) {
    assert.equal(formatData('x'), 'x');
});

QUnit.test('view model copies only responses by default', function(assert) {
    var vm = new LlamaViewModel();
    assert.ok(vm.copyOnlyResponses());
});
