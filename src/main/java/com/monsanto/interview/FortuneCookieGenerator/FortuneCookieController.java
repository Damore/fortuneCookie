package com.monsanto.interview.FortuneCookieGenerator;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FortuneCookieController {

	@Autowired
	private QuoteRepository quoteRepository;

	// The blocker issue was caused by injecting a prototype bean into a singleton instance.
	@Autowired
	private ObjectFactory<FortuneCookieBuilder> fortuneCookieBuilderFactory;

	@GetMapping(path = "/generateFortuneCookie", produces = { "application/json" })
	public @ResponseBody FortuneCookie generateFortuneCookie(@RequestParam("client") String client,
			@RequestParam("company") String company) {
		String quote = quoteRepository.getRandomQuote();
		FortuneCookie fortuneCookie = getPrototypeInstance().withClient(client).withCompany(company).withQuote(quote)
				.build();
		return fortuneCookie;
	}
	
	/**
	 * Now, for each request, a new fortuneCookieBuilder is created.
	 * 
	 * @return the forturneCookieBuilder
	 */
	public FortuneCookieBuilder getPrototypeInstance() {
		return fortuneCookieBuilderFactory.getObject();
	}

}
