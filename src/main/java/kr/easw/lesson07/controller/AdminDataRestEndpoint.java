package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.TextDataDto;
import kr.easw.lesson07.service.TextDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/data/admin")
@RequiredArgsConstructor
public class AdminDataRestEndpoint {
    private final TextDataService textDataService;
    @PostMapping("/add")
    public ModelAndView addText(@RequestParam("text") String text) {
        textDataService.addText(new TextDataDto(0L, text));
        return new ModelAndView("redirect:/admin?success=true");
    }

}
