<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:useBean id="HomeVar" scope="request" class="com.ko.na.HomeImage"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- Website template by freewebsitetemplates.com -->
<html>
<head>
	<title>Family Tree</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
</head>
<body>
	<div id="header">
		<div class="clearfix">
			<div class="logo">
				<a href="index.html"><img src="images/CFT-Logo-web-inverted.png" alt="LOGO" height="52" width="364"></a>
			</div>
			<ul class="navigation">
				<li class="active">
					<a href="index.html">Home</a>
				</li>
				<li>
					<a href="about.html">About</a>
				</li>
				<li>
					<a href="practices.html">Practices</a>
				</li>
				<li>
					<a href="lawyers.html">Our Lawyers</a>
				</li>
				<li>
					<a href="news.html">News</a>
					<div>
						<a href="singlepost.html">News Single Post</a>
					</div>
				</li>
				<li>
					<a href="contact.html">Contact</a>
				</li>
			</ul>
		</div>
	</div>
	<div id="contents">
		<div id="adbox">
			<div class="clearfix">
			    <jsp:getProperty name='HomeVar' property='imageTag'/>
				<div class="detail">
				<img src="images/Though_many.png" >
				</div>
			</div>
		</div>
		<div class="highlight">
			<div class="clearfix">
				<div class="testimonial">
					<h2>Testimonials</h2>
					<p>
						&ldquo;Aenean ullamcorper purus vitae nisl tristique sollicitudin. Quisque vestibulum, erat ornare.&rdquo;
					</p>
					<span>-John Doe and Jane Doe-</span>
				</div>
				<h1>Pass on the Legacy</h1>
				<p>
					This website template has been designed by <a href="http://www.freewebsitetemplates.com/">Free Website Templates</a> for you, for free. You can replace all this text with your own text. You can remove any link to our website from this website template, you're free to use this website template without linking back to us. If you're having problems editing this website template, then don't hesitate to ask for help on the <a href="http://www.freewebsitetemplates.com/forums/">Forums</a>.
				</p>
			</div>
		</div>
		<div class="featured">
			<h2>Why Choose Us?</h2>
			<ul class="clearfix">
				<li>
					<div class="frame1">
						<div class="box">
							<img src="images/meeting.jpg" alt="Img" height="130" width="197">
						</div>
					</div>
					<p>
						<b>Our lawyers</b> Our website templates are created with inspiration, checked for quality and originality.
					</p>
					<a href="index.html" class="more">Read More</a>
				</li>
				<li>
					<div class="frame1">
						<div class="box">
							<img src="images/handshake.jpg" alt="Img" height="130" width="197">
						</div>
					</div>
					<p>
						<b>get to know us more</b> Just browse through all our Free Website Templates and ﬁnd what you’re looking for.
					</p>
					<a href="index.html" class="more">Read More</a>
				</li>
				<li>
					<div class="frame1">
						<div class="box">
							<img src="images/smile.jpg" alt="Img" height="130" width="197">
						</div>
					</div>
					<p>
						<b>what we offer</b> Join the discussion on our forum and meet other people in the community.
					</p>
					<a href="index.html" class="more">Read More</a>
				</li>
				<li>
					<div class="frame1">
						<div class="box">
							<img src="images/family-small.jpg" alt="Img" height="130" width="197">
						</div>
					</div>
					<p>
						<b>get in touch with us</b> And we love the challenge of doing something diferent and something special.
					</p>
					<a href="index.html" class="more">Read More</a>
				</li>
			</ul>
		</div>
	</div>
	<div id="footer">
		<div class="clearfix">
			<div class="section">
				<h4>Latest News</h4>
				<p>
					This website template has been designed by Free Website Templates for you, for free. You can replace all this text with your own text. You can remove any link.
				</p>
			</div>
			<div class="section contact">
				<h4>Contact Us</h4>
				<p>
					<span>Address:</span> the address city, state 1111
				</p>
				<p>
					<span>Phone:</span> (+20) 000 222 999
				</p>
				<p>
					<span>Email:</span> info@freewebsitetemplates.com
				</p>
			</div>
			<div class="section">
				<h4>SEND US A MESSAGE</h4>
				<p>
					If you're having problems editing this website template, then don't hesitate to ask for help on the Forums.
				</p>
				<a href="http://www.freewebsitetemplates.com/misc/contact/" class="subscribe">Click to send us an email</a>
			</div>
		</div>
		<div id="footnote">
			<div class="clearfix">
				<div class="connect">
					<a href="http://freewebsitetemplates.com/go/facebook/" class="facebook"></a><a href="http://freewebsitetemplates.com/go/twitter/" class="twitter"></a><a href="http://freewebsitetemplates.com/go/googleplus/" class="googleplus"></a><a href="http://pinterest.com/fwtemplates/" class="pinterest"></a>
				</div>
				<p>
					© Copyright 2023 Manes Winchester. All Rights Reserved.
				</p>
			</div>
		</div>
	</div>
</body>
</html>