package com.blyweertboukari.studentcollab.student.controller;

import com.blyweertboukari.studentcollab.student.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@Tag(name = "reviews", description = "Review API")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

}
