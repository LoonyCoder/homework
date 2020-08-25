local command = "/usr/local/bin/gm convert " .. ngx.var.org_file .. " -thumbnail " .. ngx.var.width .. "x" .. ngx.var.height .. " " .. ngx.var.file;
os.execute(command);
ngx.exec(ngx.var.request_uri);