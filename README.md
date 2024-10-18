# scittle-emmy-viewers-demo

This is demo of [scittle](https://github.com/babashka/scittle)'s
[emmy-viewers](https://github.com/mentat-collective/emmy-viewers) plugins.

You can see cloudflare page hosted of this repo
[here](https://ev-demo.datafy.id).

## Development

### REPL

To develop with connected nrepl e.g. from emacs, follow below instruction.

1. Start babashka dev task.

```bash
bb dev
```

3. Wait for some time and browse to http://localhost:2341/repl.html. It will create
   connection between sci nrepl server to browser js runtime.

4. From emacs, open any cljs file, e.g. `./public/cljs/hello.cljs`, then
   type `M-x cider-connect-clj RET localhost RET 2339 RET`. At this point, the repl
   should be started successfully but the source buffer was not linked into
   repl buffer yet.

5. Go to repl repl buffer with `M-x switch-to-buffer`, and pick the correct repl
   that is started in 3.

6. Set the repl buffer to cljs by typing `M-x cider-set-repl-type RET cljs RET`.

7. Switch back to source buffer with `M-x switch-to-buffer` again.

8. At this point, the source and repl buffer now linked so that we can for example
   type `C-c C-z` to switch back and forth between source and repl buffer.

### Bun + Vite

We use `vite` and `bun` here to manage static html pages.

```bash
bun install
```

To start dev environment:

```bash
bun install
bun run dev -- --port 2342
```

Then browse to http://localhost:2342.

To deploy/publish to cloudflare:

```bash
bun run publish
```

