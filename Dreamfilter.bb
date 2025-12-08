Global ark_blur_image%, ark_blur_texture%, ark_sw%, ark_sh%
Global ark_blur_cam%
Global ark_bufSize#
Global ark_bufHalf#

Function CreateBlurImage()
	
	; pot buffer to have it stable at any resolution
	ark_bufSize = 512.0
	While ark_bufSize < GraphicWidth Or ark_bufSize < GraphicHeight
		ark_bufSize = ark_bufSize * 2.0
	Wend
	
	ark_bufHalf = ark_bufSize * 0.5
	
	;Create blur Camera
	Local cam% = CreateCamera()
	CameraProjMode cam,2
	CameraZoom cam,0.1
	CameraClsMode cam, 0, 0
	CameraRange cam, 0.1, 1.5
	MoveEntity cam, 0, 0, 10000
	ark_blur_cam = cam
	
	ark_sw = GraphicWidth;GraphicsWidth()
	ark_sh = GraphicHeight;GraphicsHeight()
	CameraViewport cam,0,0,ark_sw,ark_sh
	
	;Create sprite
	Local spr% = CreateMesh(cam)
	Local sf% = CreateSurface(spr)
	AddVertex sf, -1, 1, 0, 0, 0
	AddVertex sf, 1, 1, 0, 1, 0
	AddVertex sf, -1, -1, 0, 0, 1
	AddVertex sf, 1, -1, 0, 1, 1
	AddTriangle sf, 0, 1, 2
	AddTriangle sf, 3, 2, 1
	EntityFX spr, 17
	EntityOrder spr, -100000
	EntityBlend spr, 1
	
	; compensate for pot so the texture properly aligns
	Local sx# = ark_bufSize / Float(ark_sw)
	Local sy# = ark_bufSize / Float(ark_sh)
	ScaleEntity spr, sx, sy, 1.0
	PositionEntity spr, 0, 0, 1.0001
	
	ark_blur_image = spr
	;Create blur texture
	ark_blur_texture = CreateTexture(ark_bufSize, ark_bufSize, 256)
	EntityTexture spr, ark_blur_texture
End Function

Function UpdateBlur(power#)
	
    EntityAlpha ark_blur_image, power#
	
    Local ox# = ark_bufHalf - (GraphicWidth / 2.0)
    Local oy# = ark_bufHalf - (GraphicHeight / 2.0)
	;CopyRect ark_sw / 2 - 1024, ark_sh / 2 - 1024, 2048, 2048, 0, 0, BackBuffer(), TextureBuffer(ark_blur_texture)
	;CopyRect 0, 0, GraphicWidth, GraphicHeight, 1024.0 - GraphicWidth/2, 1024.0 - GraphicHeight/2, BackBuffer(), TextureBuffer(ark_blur_texture)
    CopyRect 0, 0, GraphicWidth, GraphicHeight, ox, oy, BackBuffer(), TextureBuffer(ark_blur_texture)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D