class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/api/baseContent/create" {
                    controller = "auth"
                    action = "test2"
                }

        "/application" {
	controller = "auth"
	action = "app"
        }

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
