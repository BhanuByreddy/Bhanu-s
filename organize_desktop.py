import os
import shutil

source_path = os.path.join(os.environ.get('USERPROFILE'),'Desktop')

file_organize = {
    '5th Sem': {
        'OperatingSystems': ['_os','os.docx'],
        'Linux':['_li','li.docx'],
        'Computer Networks': ['_cn','cn.docx'],
        'Fundamentals of CryptoGraphy and CryptAnalysis':['_cg','_pt','_ct','cry'],
        'AssignmentProgrammes': ['_ap','.txt'],
        'DSA': ['.cpp','ds.docx'],
        'Python':['.py','ds.docx'],
        'Striver TUF V3+':['_st.txt','_st','str'],
        'Resources':['_5.txt','_5th.txt','.txt']
    },
    '4th Sem': {
        'Design and Analysis of Algorithms': ['_daa'],
        'Computer Architecture': ['_ca'],
        'Data Base Management System': ['_db'],
        'Discrete Structures': ['_dis'],
        'Maths for CyberSecurity':['_mcs'],
        'Documents': ['.pdf', '.jpg','Bhanu','receipt','Resume'],
        'MERN stack':['.html', '.css', '.js','.mern'],
        'Resources':['4.txt','4th.txt','.txt']
    },
    'Images': ['.jpg', '.jpeg', '.png'],
    'Videos': ['.mp4', '.mkv'],
    'ArchiveFiles': ['.zip', '.rar'],
    'Other':[]
}
skip_extensions = [
    '.lnk', '.url', '.exe', '.msi',
    '.ini', '.tmp', '.log', '.desktop',
    '.bat', '.cmd', '.ico', '.dll'
]                                            # this or some sort of skip extensions which we need to skip
content = os.listdir(source_path) # We get the Content present on the Desktop
for item in content:
    item_path = os.path.join(source_path,item) # Folder Path
    if os.path.isdir(item_path):  # if it is a folder
        continue

    _,extension = os.path.splitext(item)  # since skip extensions are in lower level
    extension = extension.lower()
    if extension in skip_extensions:
        continue
    
    moved = False
    for folder,contents in file_organize.items():
        if isinstance(contents,dict):
            for Subject,extens in contents.items():
                for exten in extens:
                    if exten.lower() in item.lower():
                        subject_path=os.path.join(source_path,folder,Subject)
                        os.makedirs(subject_path,exist_ok=True)
                        shutil.move(item_path,os.path.join(subject_path,item))
                        moved=True
                        break
                if moved:
                    break
            if moved:
                break
        else:
            for ext in contents:            # remaining other extensions
                if item.lower().endswith(ext):
                    folder_path = os.path.join(source_path, folder)
                    os.makedirs(folder_path, exist_ok=True)
                    shutil.move(item_path,os.path.join(folder_path,item))
                    moved = True
                    break
            if moved:
                break
    if not moved:
        other_path = os.path.join(source_path,'Other')
        os.makedirs(other_path,exist_ok=True)
        shutil.move(item_path, os.path.join(other_path, item))
        
                    
                    
                
                
            
        

